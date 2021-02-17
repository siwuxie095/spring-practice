package com.siwuxie095.spring.chapter9th.example10th;

/**
 * @author Jiajing Li
 * @date 2021-02-17 16:56:36
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 配置自定义的用户服务
     *
     * 假设需要认证的用户存储在非关系型数据库中，如 Mongo 或 Neo4j，在这种情况下，需要提供一个自定义的
     * UserDetailsService 接口实现。
     *
     * UserDetailsService 接口非常简单：
     *
     * public interface UserDetailsService {
     *
     *     UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
     *
     * }
     *
     * 所需要做的就是实现 loadUserByUsername() 方法，根据给定的用户名来查找用户。loadUserByUsername()
     * 方法会返回代表给定用户的 UserDetails 对象。如下代码展现了一个 UserDetailsService 的实现，它会从
     * 给定的 SpitterRepository 实现中查找用户。
     *
     * public class SpitterUserService implements UserDetailsService {
     *
     *     private final SpitterRepository spitterRepository;
     *
     *     public SpitterUserService(SpitterRepository spitterRepository) {
     *         this.spitterRepository = spitterRepository;
     *     }
     *
     *     @Override
     *     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     *         Spitter spitter = spitterRepository.findByUsername(username);
     *         if (spitter != null) {
     *             List<GrantedAuthority> authorities = new ArrayList<>();
     *             authorities.add(new SimpleGrantedAuthority("ROLE_SPITTER"));
     *             return new User(spitter.getUsername(), spitter.getPassword(), authorities);
     *         }
     *         throw new UsernameNotFoundException("User '" + username + "' not found.");
     *     }
     *
     * }
     *
     * SpitterUserService 有意思的地方在于它并不知道用户数据存储在什么地方。设置进来的 SpitterRepository
     * 能够从关系型数据库、文档数据库或图数据中查找 Spitter 对象，甚至可以伪造一个。SpitterUserService 不
     * 知道也不会关心底层所使用的数据存储。它只是获得 Spitter 对象，并使用它来创建 User 对象。
     *
     * PS：User 是 UserDetails 的具体实现。
     *
     * 为了使用 SpitterUserService 来认证用户，我们可以通过 userDetailsService() 方法将其设置到安全配置
     * 中：
     *
     * @Configuration
     * @EnableWebMvcSecurity
     * public class SecurityConfig extends WebSecurityConfigurerAdapter {
     *
     *     @Autowired
     *     private SpitterRepository spitterRepository;
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.userDetailsService(new SpitterUserService(spitterRepository));
     *     }
     *
     * }
     *
     * userDetailsService() 方法（类似于 jdbcAuthentication()、ldapAuthentication() 以及
     * inMemoryAuthentication()）会配置一个用户存储。不过，这里所使用的不是 Spring 所提供的用户存储，而是
     * 使用 UserDetailsService 的实现。
     *
     * 另外一种值得考虑的方案就是修改 Spitter，让其实现 UserDetails。这样的话，loadUserByUsername() 就能
     * 直接返回 Spitter 对象了，而不必再将它的值复制到 User 对象中。
     */
    public static void main(String[] args) {

    }

}
