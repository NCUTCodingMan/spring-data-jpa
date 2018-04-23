package com.rain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * 在使用Spring Data JPA的过程中遇到的问题
 * 1.pom.xml出了引用spring-data-jpa.jar之外, 还需要引入其他的jar么
 *  spring-data-jpa底层可以基于不同的实现, 例如Hibernate, OpenJPA等等. 在Maven仓库中搜索spring-data-jpa.jar的依赖时, 简单看看
 *  当前jar的间接依赖关系. 可以通过mvn dependency:tree查看当前的依赖关系.
 *  Hibernate & OpenJPA是可选项, 即不会加入到当前的Classpath下
 *
 * 2.JpaVendorAdapter
 *  用于指明所使用的是哪一个厂商的JPA实现, Spring提供了多个JPA厂商适配器
 *
 * 3.JpaRepository<T, ID>
 *  在将Spring和Hibernate集成之后发现, 部分操作, 如save(), update(), delete()...除了实体类信息不一样, 方法的其他操作完全一致,
 *  具有很强的模板特性, JpaRepository<T, ID>提供了常用的18个用于数据库操作的方法. 通过<继承>该接口, 而不用写接口的实现类. 即可
 *  拥有这18个常见的方法.
 *
 *  其继承体系如下
 *  CrudRepository  提供基本的CRUD
 *   PagingAndSortingRepository 提供分页查询与排序规则
 *    JpaRepository ...
 *
 * 3.@EnableJpaRepositories
 *  由于本项目使用基于Java的配置, 因此这里使用基于包扫描的方式扫描集成了JpaRepository的接口, 为其构建实现类
 *  也可以通过xml配置<jpa:repositories base-package=""></>
 *
 * 4.LocalContainerEntityManagerFactoryBean
 *  在Hibernate与OpenJPA中经常看见persistence.xml配置文件, 其用于配置实体与数据库表的映射信息. entityManagerFactoryBean即是
 *  用来管理这些实体的, 这里基于Java配置, 并设置相关的扫描包(com.rain.entity)
 *
 * 5.TransactionManager
 *  TransactionManager, 配置这个的原因是在进行分页查询时
 *      studentRepository.findAll(pageable)
 *  程序报错了, 提示未配置事务管理器, 这个就有点懵逼
 *
 *
 * @author rain
 * created on 2018/4/22
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.rain.repository"})
public class SpringAppInitializer {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "tiger";
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&" +
            "characterEncoding=utf8&characterSetResults=utf8";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(URL);
        return dataSource;
    }

    /**
     * 以Hibernate为JPA的具体实现
     * @return JpaVendorAdapter
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        return adapter;
    }

    /**
     * LocalContainerEntityManagerFactoryBean
     * @param adapter Hibernate
     * @param dataSource DataSource
     * @return LocalContainerEntityManagerFactoryBean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaVendorAdapter adapter, DataSource
            dataSource) {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource);
        emfb.setJpaVendorAdapter(adapter);
        // 设置扫描实体包
        emfb.setPackagesToScan("com.rain.entity");
        return emfb;
    }

    /**
     * 为什么需要配置TransactionManager...
     * @param dataSource 数据源
     * @return TransactionManager
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
}