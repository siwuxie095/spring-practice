package com.siwuxie095.spring.chapter10th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-02-21 22:16:09
 */
public class Main {

    /**
     * 了解 Spring 的数据访问异常体系
     *
     * 这里有一个关于跳伞运动员的经典笑话，这个运动员被风吹离正常路线后降落在树上并高高地挂在那里。后来，有人路过，
     * 跳伞运动员就问他自己在什么地方。过路人回答说："你在离地大约 20 尺的空中。" 跳伞运动员说："你一定是个软件
     * 分析师。" 过路人回应说："你说对了。你是怎么知道的呢？" "因为你跟我说的话百分百正确，但丝毫用处都没有。"
     *
     * 这个故事已经听过很多遍了，每次过路人的职业或国籍都会有所不同。但是这个故事使想人起了 JDBC 中的 SQLException。
     * 如果你曾经编写过 JDBC 代码（不使用 Spring），你肯定会意识到如果不强制捕获 SQLException 的话，几乎无法
     * 使用 JDBC 做任何事情。SQLException 表示在尝试访问数据库时出现了问题，但是这个异常却没有告诉你哪里出错了
     * 以及如何进行处理。
     *
     * 可能导致抛出 SQLException 的常见问题包括：
     * （1）应用程序无法连接数据库；
     * （2）要执行的查询存在语法错误；
     * （3）查询中所使用的表和/或列不存在；
     * （4）试图插入或更新的数据违反了数据库约束。
     *
     * SQLException 的问题在于捕获到它的时候该如何处理。事实上，能够触发 SQLException 的问题通常是不能在 catch
     * 代码块中解决的。大多数抛出 SQLException 的情况表明发生了致命性错误。如果应用程序不能连接到数据库，这通常
     * 意味着应用不能继续使用了。类似地，如果查询时出现了错误，那在运行时基本上也是无能为力。
     *
     * 如果无法从 SQLException 中恢复，那为什么还要强制捕获它呢？
     *
     * 即使对某些 SQLException 有处理方案，还是要捕获 SQLException 并查看其属性才能获知问题根源的更多信息。这
     * 是因为 SQLException 被视为处理数据访问所有问题的通用异常。对于所有的数据访问问题都会抛出 SQLException，
     * 而不是对每种可能的问题都会有不同的异常类型。
     *
     * 一些持久化框架提供了相对丰富的异常体系。例如，Hibernate 提供了二十个左右的异常，分别对应于特定的数据访问
     * 问题。这样就可以针对想处理的异常编写 catch 代码块。
     *
     * 即便如此，Hibernate 的异常是其本身所特有的。正如前面所言，这里想将特定的持久化机制独立于数据访问层。如果
     * 抛出了 Hibernate 所特有的异常，那对 Hibernate 的使用将会渗透到应用程序的其他部分。如果不这样做的话，就
     * 得捕获持久化平台的异常，然后将其作为平台无关的异常再次抛出。
     *
     * 一方面，JDBC 的异常体系过于简单了 —— 实际上，它算不上一个体系。另一方面，Hibernate 的异常体系是其本身所
     * 独有的。这里需要的数据访问异常要具有描述性而且又与特定的持久化框架无关。
     *
     *
     *
     * Spring 所提供的平台无关的持久化异常
     *
     * Spring JDBC 提供的数据访问异常体系解决了以上的两个问题。不同于 JDBC，Spring 提供了多个数据访问异常，
     * 分别描述了它们抛出时所对应的问题。
     *
     * JDBC 的异常如下：
     * BatchUpdateException
     * DataTruncation
     * SQLException
     * SQLWarning
     *
     * Spring 的数据访问异常如下：
     * BadSqlGrammarException
     * CannotAcquireLockException
     * CannotSerializeTransactionException
     * CannotGetJdbcConnectionException
     * CleanupFailureDataAccessException
     * ConcurrencyFailureException
     * DataAccessException
     * DataAccessResourceFailureException
     * DataIntegrityViolationException
     * DataRetrievalFailureException
     * DataSourceLookupApiUsageException
     * DeadlockLoserDataAccessException
     * DuplicateKeyException
     * EmptyResultDataAccessException
     * IncorrectResultSizeDataAccessException
     * IncorrectUpdateSemanticsDataAccessException
     * InvalidDataAccessApiUsageException
     * InvalidDataAccessResourceUsageException
     * InvalidResultSetAccessException
     * JdbcUpdateAffectedIncorrectNumberOfRowsException
     * LbRetrievalFailureException
     * NonTransientDataAccessResourceException
     * OptimisticLockingFailureException
     * PermissionDeniedDataAccessException
     * PessimisticLockingFailureException
     * QueryTimeoutException
     * RecoverableDataAccessException
     * SQLWarningException
     * SqlXmlFeatureNotImplementedException
     * TransientDataAccessException
     * TransientDataAccessResourceException
     * TypeMismatchDataAccessException
     * UncategorizedDataAccessException
     * UncategorizedSQLException
     * ...
     *
     * 可以看出，Spring 为读取和写入数据库的几乎所有错误都提供了异常。Spring 的数据访问异常要比所列的还要多。
     *
     * PS：在此没有列出所有的异常，因为不想让 JDBC 显得太寒酸。
     *
     * 尽管 Spring 的异常体系比 JDBC 简单的 SQLException 丰富得多，但它并没有与特定的持久化方式相关联。这意味
     * 着可以使用 Spring 抛出一致的异常，而不用关心所选择的持久化方案。这有助于将所选择持久化机制与数据访问层隔离
     * 开来。
     *
     *
     *
     * 看！不用写 catch 代码块
     *
     * 上面列出的Spring 的数据访问异常没有体现出来的一点就是这些异常都继承自 DataAccessException。
     *
     * DataAccessException 的特殊之处在于它是一个非检查型异常。换句话说，没有必要捕获 Spring 所抛出的数据访问
     * 异常（当然，如果你想捕获的话也是完全可以的）。
     *
     * DataAccessException 只是 Spring 处理检查型异常和非检查型异常哲学的一个范例。Spring 认为触发异常的很多
     * 问题是不能在 catch 代码块中修复的。Spring 使用了非检查型异常，而不是强制开发人员编写 catch 代码块（里面
     * 经常是空的）。这把是否要捕获异常的权力留给了开发人员。
     *
     * 为了利用 Spring 的数据访问异常，必须使用 Spring 所支持的数据访问模板。后续将会看到 Spring 的模板是如何
     * 简化数据访问的。
     */
    public static void main(String[] args) {

    }

}
