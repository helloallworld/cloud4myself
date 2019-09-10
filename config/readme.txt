
以application开头的 -dev.properties/ -test.properties为全局公用，及所有的服务都会去拉取的配置

-dev,-test,-prod是为不同开发环境准备的

producer-service为具体某个服务名，只有在某个服务中的config.name与之匹配时才能读取