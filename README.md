
阿里云分析型数据库（AnalyticDB）JDBC 驱动
============================
# 说明
> 因阿里云分析型数据库（AnalyticDB）不支持别名加星号的语句(例：`SELECT d.* FROM demo d`)，但支持不加别名的语句`SELECT * FROM demo d`，
> 导致基于JDBC的数据库客户端如果以含别名的方式就无法打开表，此项目基于mysql-connector-java的5.1.34版本做了一层驱动的包装去掉了别名
> 使客户端能正常打开表，此驱动只用于数据库GUI客户端，不用于实际开发。

# 使用
1. 添加`mysql-connector-java`和 `aliyun-analytic-db-driver`
2. 使用驱动：`com.mysql.jdbc.AnalyticDbDriver`

# 下载
[drivers.zip](https://github.com/javaercn/aliyun-analytic-db-driver/files/1218376/drivers.zip)

# jetbrains 系列的数据库客户端使用样例
![sample](docs/sample.png)

# 其它
> mysql-connector-java的5.1.34版本对阿里云分析型数据库（AnalyticDB）兼容性较好。
> [官方说明](https://help.aliyun.com/knowledge_list/35322.html)