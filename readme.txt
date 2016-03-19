Segue abaixo configuração do contexto

<Context ..........>
    <Environment name="propsLocation" value="file:////home/rodrigo/climario.local.properties" type="java.lang.String" override="false"/>
</Context>

Informações que devem conter no arquivo de propriedades
climario.hibernate.dialect = org.hibernate.dialect.H2Dialect
climario.hibernate.hbm2ddl.auto = update
climario.hibernate.show_sql = true
climario.hibernate.format_sql = false
climario.hibernate.default_schema = public

climario.database.driver = org.h2.Driver
climario.database.url = jdbc:h2:~/climario/hmg/data
climario.database.user = admin
climario.database.pass = admin