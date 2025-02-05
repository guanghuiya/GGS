package com.meiqiu.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description 代码生成器
 * @Author sgh
 * @Date 2025/1/21
 * @Time 15:26
 */
public class FastAutoGenerator {

    public static String userDir = System.getProperty("user.dir");

    public static void main(String[] args) {
        List<String> tables = new ArrayList<>();
        tables.add("user");

        com.baomidou.mybatisplus.generator.FastAutoGenerator.create("jdbc:mysql://47.96.25.123:3306/my_demo?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true", "meiqiu", "Meiqiu@123456")
                .globalConfig(builder -> {
                    builder.author("sgh")
                            .outputDir(userDir + "/src/main/java")    //输出路径(写到java目录)
                            .enableSwagger()           //开启swagger
                            .commentDate("yyyy-MM-dd hh:mm:ss")
                            .dateType(DateType.ONLY_DATE)
                            .disableOpenDir()
                            .fileOverride();            //开启覆盖之前生成的文件

                })
                .packageConfig(builder -> {
                    builder.parent("com.meiqiu")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .mapper("mapper")
                            .xml("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, userDir + "/src/main/resources/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .addTablePrefix("")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder()
                            .enableLombok()
                            .logicDeleteColumnName("deleted")
                            .enableTableFieldAnnotation()
                            .controllerBuilder()
                            // 映射路径使用连字符格式，而不是驼峰
                            .enableHyphenStyle()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            .mapperBuilder()
                            //生成通用的resultMap
                            .enableBaseResultMap()
                            .superClass(BaseMapper.class)
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                    builder.entityBuilder()
                            .versionColumnName("version")
                            .versionPropertyName("version")
                            .formatFileName("%s");
                })
                .templateConfig(builder -> {
                    // 实体类使用我们自定义模板
                    builder.entity("templates/entity.java");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
