package com.siwuxie095.spring.chapter10th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-02-23 08:20:04
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 在 Spring 中使用 JDBC
     *
     * 持久化技术有很多种，而 Hibernate、iBATIS 和 JPA 只是其中的几种而已。尽管如此，还是有很多的应用程序
     * 使用最古老的方式将 Java 对象保存到数据库中：他们自食其力。不，等等，这是他们挣钱的途径。这种久经考验
     * 并证明行之有效的持久化方法就是古老的 JDBC。
     *
     * 为什么不采用它呢？JDBC 不要求掌握其他框架的查询语言。它是建立在 SQL 之上的，而 SQL 本身就是数据访问
     * 语言。此外，与其他的技术相比，使用 JDBC 能够更好地对数据访问的性能进行调优。JDBC 允许你使用数据库的
     * 所有特性，而这是其他框架不鼓励甚至禁止的。
     *
     * 再者，相对于持久层框架，JDBC 能够在更低的层次上处理数据，这样可以完全控制应用程序如何读取和管理数据，
     * 包括访问和管理数据库中单独的列。这种细粒度的数据访问方式在很多应用程序中是很方便的。例如在报表应用中，
     * 如果将数据组织为对象，而接下来唯一要做的就是将其解包为原始数据，那就没有太大意义了。
     *
     * 但是 JDBC 也不是十全十美的。虽然 JDBC 具有强大、灵活和其他一些优点，但也有其不足之处。
     *
     *
     *
     * 1、应对失控的 JDBC 代码
     *
     * 如果使用 JDBC 所提供的直接操作数据库的 API，你需要负责处理与数据库访问相关的所有事情，其中包含管理数
     * 据库资源和处理异常。如果你曾经使用 JDBC 往数据库中插入数据，那如下代码对你应该并不陌生：
     *
     *     private static final String SQL_INSERT_SPITTER =
     *             "insert into spitter (username, password, fullname) values (?, ?, ?)";
     *
     *     private DataSource dataSource;
     *
     *     public void addSpitter(Spitter spitter) {
     *         Connection conn = null;
     *         PreparedStatement stmt = null;
     *         try {
     *             conn = dataSource.getConnection();
     *             stmt = conn.prepareStatement(SQL_INSERT_SPITTER);
     *             stmt.setString(1, spitter.getUsername());
     *             stmt.setString(2, spitter.getPassword());
     *             stmt.setString(3, spitter.getFullName());
     *             stmt.execute();
     *         } catch (SQLException e) {
     *             // do something ... not sure what, though
     *         } finally {
     *             try {
     *                 if (stmt != null) {
     *                     stmt.close();
     *                 }
     *                 if (conn != null) {
     *                     conn.close();
     *                 }
     *             } catch (SQLException e) {
     *                 // I'm even less sure about what to do here
     *             }
     *         }
     *     }
     *
     * 看看这些失控的代码！这个超过 20 行的代码仅仅是为了向数据库中插入一个简单的对象。对于 JDBC 操作来讲，
     * 这应该是最简单的了。但是为什么要用这么多行代码才能做如此简单的事情呢？实际上，并非如此，只有几行代码
     * 是真正用于进行插入数据的。但是 JDBC 要求你必须正确地管理连接和语句，并以某种方式处理可能抛出的
     * SQLException 异常。
     *
     * 再提一句这个 SQLException 异常：你不但不清楚如何处理它（因为并不知道哪里出错了），而且你还要捕捉它
     * 两次！你需要在插入记录出错时捕捉它，同时你还需要在关闭语句和连接出错的时候捕捉它。看起来我们要做很多
     * 的工作来处理可能出现的问题，而这些问题通常是难以通过编码来处理的。
     *
     * 再来看一下如下代码，使用传统的 JDBC 来更新数据库中 Spitter 表的一行。
     *
     *     private static final String SQL_UPDATE_SPITTER =
     *             "update spitter set username = ?, password = ?, fullname = ? where id = ?";
     *
     *     public void saveSpitter(Spitter spitter) {
     *         Connection conn = null;
     *         PreparedStatement stmt = null;
     *         try {
     *             conn = dataSource.getConnection();
     *             stmt = conn.prepareStatement(SQL_UPDATE_SPITTER);
     *             stmt.setString(1, spitter.getUsername());
     *             stmt.setString(2, spitter.getPassword());
     *             stmt.setString(3, spitter.getFullName());
     *             stmt.setLong(4, spitter.getId());
     *             stmt.execute();
     *         } catch (SQLException e) {
     *             // Still not sure what I'm supposed to do here
     *         } finally {
     *             try {
     *                 if (stmt != null) {
     *                     stmt.close();
     *                 }
     *                 if (conn != null) {
     *                     conn.close();
     *                 }
     *             } catch (SQLException e) {
     *                 // or here
     *             }
     *         }
     *     }
     *
     * 乍看上去，这两段代码是相同的。实际上，除了 SQL 字符串和创建语句的那一行，它们是完全相同的。同样，这里
     * 也使用大量代码来完成一件简单的事情，而且有很多重复的代码。在理想情况下，只需要编写与特定任务相关的代码。
     * 毕竟，这才是这两段代码的不同之处，剩下的都是样板代码。
     *
     * 为了完成对 JDBC 的完整介绍，下面看一下如何从数据库中获取数据。如下所示，它也不简单。
     *
     *     private static final String SQL_SELECT_SPITTER =
     *             "select id, username, fullname from spitter where id = ?";
     *
     *     public Spitter findOne(long id) {
     *         Connection conn = null;
     *         PreparedStatement stmt = null;
     *         ResultSet rs = null;
     *         try {
     *             conn = dataSource.getConnection();
     *             stmt = conn.prepareStatement(SQL_SELECT_SPITTER);
     *             stmt.setLong(1, id);
     *             rs = stmt.executeQuery();
     *             Spitter spitter = null;
     *             if (rs.next()) {
     *                 spitter = new Spitter();
     *                 spitter.setId(rs.getLong("id"));
     *                 spitter.setUsername(rs.getString("username"));
     *                 spitter.setPassword(rs.getString("password"));
     *                 spitter.setFullName(rs.getString("fullname"));
     *             }
     *             return spitter;
     *         } catch (SQLException e) {
     *             // ...
     *         } finally {
     *             if (rs != null) {
     *                 try {
     *                     rs.close();
     *                 } catch (SQLException e) {}
     *             }
     *             if (stmt != null) {
     *                 try {
     *                     stmt.close();
     *                 } catch (SQLException e) {}
     *             }
     *             if (conn != null) {
     *                 try {
     *                     conn.close();
     *                 } catch (SQLException e) {}
     *             }
     *         }
     *         return null;
     *     }
     *
     * 这段代码与插入和更新的样例一样冗长，甚至更为复杂。这就好像 Pareto 法则被倒了过来：只有 20% 的代码是
     * 真正用于查询数据的，而 80% 代码都是样板代码。
     *
     * 现在你可以看出，大量的 JDBC 代码都是用于创建连接和语句以及异常处理的样板代码。既然已经得出了这个观点，
     * 就将不再接受它的折磨，以后你再也不会看到这样令人厌恶的代码了。
     *
     * 但实际上，这些样板代码是非常重要的。清理资源和处理错误确保了数据访问的健壮性。如果没有它们的话，就不会
     * 发现错误而且资源也会处于打开的状态，这将会导致意外的代码和资源泄露。所以不仅需要这些代码，而且还要保证
     * 它是正确的。基于这样的原因，才需要框架来保证这些代码只写一次而且是正确的。
     *
     *
     *
     * 2、使用 JDBC 模板
     *
     * Spring 的 JDBC 框架承担了资源管理和异常处理的工作，从而简化了 JDBC 代码，使得只需编写从数据库读写数
     * 据的必需代码。
     *
     * Spring 将数据访问的样板代码抽象到模板类之中。Spring 为 JDBC 提供了三个模板类供选择：
     * （1）JdbcTemplate：最基本的 Spring JDBC 模板，这个模板支持简单的 JDBC 数据库访问功能以及基于索引
     * 参数的查询；
     * （2）NamedParameterJdbcTemplate：使用该模板类执行查询时可以将值以命名参数的形式绑定到 SQL 中，而
     * 不是使用简单的索引参数；
     * （3）SimpleJdbcTemplate：该模板类利用 Java 5 的一些特性如自动装箱、泛型以及可变参数列表来简化 JDBC
     * 模板的使用。
     *
     * 以前，在选择哪一个 JDBC 模板的时候，需要仔细权衡。但是从 Spring 3.1 开始，做这个决定变得容易多了。
     * SimpleJdbcTemplate 已经被废弃了，其 Java 5 的特性被转移到了 JdbcTemplate 中，并且只有在你需要
     * 使用命名参数的时候，才需要使用 NamedParameterJdbcTemplate。这样的话，对于大多数的 JDBC 任务来说，
     * JdbcTemplate 就是最好的可选方案，这也是这里所关注的方案。
     *
     *
     * 2.1、使用 JdbcTemplate 来插入数据
     *
     * 为了让 JdbcTemplate 正常工作，只需要为其设置 DataSource 就可以了，这使得在 Spring 中配置 JdbcTemplate
     * 非常容易，如下面的 @Bean 方法所示：
     *
     *     @Bean
     *     public JdbcTemplate jdbcTemplate(DataSource dataSource) {
     *         return new JdbcTemplate(dataSource);
     *     }
     *
     * 在这里，DataSource 是通过构造器参数注入进来的。这里所引用的 dataSource bean 可以是 javax.sql.DataSource
     * 的任意实现。
     *
     * 现在，可以将 jdbcTemplate 装配到 Repository 中并使用它来访问数据库。例如，SpitterRepository 使用了
     * JdbcTemplate：
     *
     * @Repository
     * public class JdbcSpitterRepository implements SpitterRepository {
     *
     *     private JdbcOperations jdbcOperations;
     *
     *     @Inject
     *     public JdbcSpitterRepository(JdbcOperations jdbcOperations) {
     *         this.jdbcOperations = jdbcOperations;
     *     }
     *
     *     // ...
     *
     * }
     *
     * 在这里，JdbcSpitterRepository 类上使用了 @Repository 注解，这表明它将会在组件扫描的时候自动创建。
     * 它的构造器上使用了 @Inject 注解，因此在创建的时候，会自动获得一个 JdbcOperations 对象。JdbcOperations
     * 是一个接口，定义了 JdbcTemplate 所实现的操作。通过注入 JdbcOperations，而不是具体的 JdbcTemplate，
     * 能够保证 JdbcSpitterRepository 通过 JdbcOperations 接口达到与 JdbcTemplate 保持松耦合。
     *
     * 作为另外一种组件扫描和自动装配的方案，可以将 JdbcSpitterRepository 显式声明为 Spring 中的 bean，
     * 如下所示：
     *
     *     @Bean
     *     public SpitterRepository spitterRepository(JdbcTemplate jdbcTemplate) {
     *         return new JdbcSpitterRepository(jdbcTemplate);
     *     }
     *
     * 在 Repository 中具备可用的 JdbcTemplate 后，可以极大地简化之前失控的 addSpitter() 方法。基于
     * JdbcTemplate 的 addSpitter() 方法如下：
     *
     *     private static final String INSERT_SPITTER =
     *             "insert into Spitter (username, password, fullname, email, updateByEmail) " +
     *                     "values (?, ?, ?, ?, ?)";
     *
     *     public void addSpitter(Spitter spitter) {
     *         jdbcTemplate.update(INSERT_SPITTER,
     *                 spitter.getUsername(),
     *                 spitter.getPassword(),
     *                 spitter.getFullName(),
     *                 spitter.getEmail(),
     *                 spitter.isUpdateByEmail());
     *     }
     *
     * 这个版本的 addSpitter() 方法简单多了。这里没有了创建连接和语句的代码，也没有异常处理的代码，只剩下
     * 单纯的数据插入代码。
     *
     * 不能因为你看不到这些样板代码，就意味着它们不存在。样板代码被巧妙地隐藏到 JDBC 模板类中了。当 update()
     * 方法被调用的时候 JdbcTemplate 将会获取连接、创建语句并执行插入 SQL。
     *
     * 在这里，你也看不到对 SQLException 处理的代码。在内部，JdbcTemplate 将会捕获所有可能抛出的 SQLException，
     * 并将通用的 SQLException 转换为更明确的数据访问异常，然后将其重新抛出。因为 Spring 的数据访问异常都是运行时
     * 异常，所以不必在 addSpitter() 方法中进行捕获。
     *
     *
     * 2.2、使用 JdbcTemplate 来读取数据
     *
     * JdbcTemplate 也简化了数据的读取操作。如下代码展现了新版本的 findOne() 方法，它使用了 JdbcTemplate
     * 的回调，实现根据 ID 查询 Spitter，并将结果集映射为 Spitter 对象。
     *
     *     private static final String SELECT_SPITTER =
     *             "select id, username, password, fullname, email, updateByEmail from Spitter";
     *
     *     public Spitter findOne(long id) {
     *         return jdbcTemplate.queryForObject(
     *                 SELECT_SPITTER + " where id=?", new SpitterRowMapper(), id);
     *     }
     *
     *     private static final class SpitterRowMapper implements RowMapper<Spitter> {
     *         @Override
     *         public Spitter mapRow(ResultSet rs, int rowNum) throws SQLException {
     *             long id = rs.getLong("id");
     *             String username = rs.getString("username");
     *             String password = rs.getString("password");
     *             String fullName = rs.getString("fullname");
     *             String email = rs.getString("email");
     *             boolean updateByEmail = rs.getBoolean("updateByEmail");
     *             return new Spitter(id, username, password, fullName, email, updateByEmail);
     *         }
     *     }
     *
     * 在这个 findOne() 方法中使用了 JdbcTemplate 的 queryForObject() 方法来从数据库查询 Spitter。
     * queryForObject() 方法有三个参数：
     * （1）String 对象，包含了要从数据库中查找数据的 SQL；
     * （2）RowMapper 对象，用来从 ResultSet 中提取数据并构建域对象（本例中为 Spitter）；
     * （3）可变参数列表，列出了要绑定到查询上的索引参数值。
     *
     * 真正奇妙的事情发生在 SpitterRowMapper 对象中，它实现了 RowMapper 接口。对于查询返回的每一行数据，
     * JdbcTemplate 将会调用 RowMapper 的 mapRow() 方法，并传入一个 ResultSet 和包含行号的整数。在
     * SpitterRowMapper 的 mapRow() 方法中，创建了 Spitter 对象并将 ResultSet 中的值填充进去。
     *
     * 就像 addSpitter() 那样，findOne() 方法也不用写 JDBC 模板代码。不同于传统的 JDBC，这里没有资源
     * 管理或者异常处理代码。使用 JdbcTemplate 的方法只需关注于如何从数据库中获取 Spitter 对象即可。
     *
     *
     * 2.3、在 JdbcTemplate 中使用 Java 8 的 Lambda 表达式
     *
     * 因为 RowMapper 接口只声明了 addRow() 这一个方法，因此它完全符合函数式接口（functional interface）
     * 的标准。这意味着如果使用 Java 8 来开发应用的话，可以使用 Lambda 来表达 RowMapper 的实现，而不必再
     * 使用具体的实现类了。
     *
     * 例如，findOne() 方法可以使用 Java 8 的 Lambda 表达式改写，如下所示：
     *
     *     public Spitter findOne(long id) {
     *         return jdbcTemplate.queryForObject(
     *                 SELECT_SPITTER + " where id=?", (rs, rowNum) -> {
     *                     return new Spitter(
     *                             rs.getLong("id"),
     *                             rs.getString("username"),
     *                             rs.getString("password"),
     *                             rs.getString("fullName"),
     *                             rs.getString("email"),
     *                             rs.getBoolean("updateByEmail")
     *                     );
     *                 },
     *                 id);
     *     }
     *
     * 可以看到，Lambda 表达式要比完整的 RowMapper 实现更为易读，不过它们的功能是相同的。Java 会限制
     * RowMapper 中的 Lambda 表达式，使其满足所传入的参数。
     *
     * 另外，还可以使用 Java 8 的方法引用，在单独的方法中定义映射逻辑：
     *
     *     public Spitter findOne(long id) {
     *         return jdbcTemplate.queryForObject(
     *                 SELECT_SPITTER + " where id=?", this::mapSpitter, id);
     *     }
     *
     *     public Spitter mapSpitter(ResultSet rs, int rowNum) throws SQLException {
     *         return new Spitter(
     *                 rs.getLong("id"),
     *                 rs.getString("username"),
     *                 rs.getString("password"),
     *                 rs.getString("fullName"),
     *                 rs.getString("email"),
     *                 rs.getBoolean("updateByEmail")
     *         );
     *     }
     *
     * 不管采用哪种方式，都不必显式实现 RowMapper 接口，但是与实现 RowMapper 类似，这里所提供的 Lambda
     * 表达式和方法必须要接受相同的参数，并返回相同的类型。
     *
     *
     * 2.4、使用命名参数
     *
     * 在上面的 addSpitter() 方法使用了索引参数。这意味着需要留意查询中参数的顺序，在将值传递给 update()
     * 方法的时候要保持正确的顺序。如果在修改 SQL 时更改了参数的顺序，那还需要修改参数值的顺序。
     *
     * 除了这种方法之外，还可以使用命名参数。命名参数可以赋予 SQL 中的每个参数一个明确的名字，在绑定值到查询
     * 语句的时候就通过该名字来引用参数。例如，假设 SQL_INSERT_SPITTER 查询语句是这样定义的：
     *
     *     private static final String SQL_INSERT_SPITTER =
     *             "insert into spitter (username, password, fullname) " +
     *                     "values (:username, :password, :fullname)";
     *
     * 使用命名参数查询，绑定值的顺序就不重要了，可以按照名字来绑定值。如果查询语句发生了变化导致参数的顺序与
     * 之前不一致，就不需要修改绑定的代码。
     *
     * NamedParameterJdbcTemplate 是一个特殊的 JDBC 模板类，它支持使用命名参数。在 Spring 中，
     * NamedParameterJdbcTemplate 的声明方式与常规的 JdbcTemplate 几乎完全相同：
     *
     *     @Bean
     *     public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
     *         return new NamedParameterJdbcTemplate(dataSource);
     *     }
     *
     * 在这里，将 NamedParameterJdbcOperations（NamedParameterJdbcTemplate 所实现的接口）注入到
     * Repository 中，用它来替代 JdbcOperations。现在的 addSpitter() 方法如下所示：
     *
     *     public void addSpitter(Spitter spitter) {
     *         Map<String, Object> paramMap = new HashMap<>();
     *         paramMap.put("username", spitter.getUsername());
     *         paramMap.put("password", spitter.getPassword());
     *         paramMap.put("fullname", spitter.getFullName());
     *         paramMap.put("email", spitter.getEmail());
     *         paramMap.put("updateByEmail", spitter.isUpdateByEmail());
     *         jdbcTemplate.update(INSERT_SPITTER, paramMap);
     *     }
     *
     * 这个版本的 addSpitter() 比前一版本的代码要长一些。这是因为命名参数是通过 java.util.Map 来进行绑定
     * 的。不过，每行代码都关注于往数据库中插入 Spitter 对象。这个方法的核心功能并不会被资源管理或异常处理
     * 这样的代码所充斥。
     */
    public static void main(String[] args) {

    }

}
