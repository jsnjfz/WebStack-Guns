package com.jsnjfz.manage.core.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RuntimeDependencySurfaceTest {

    @Test
    void excludesGeneratorAndFastjsonOneFromRuntimeClasspath() {
        ClassLoader classLoader = getClass().getClassLoader();

        assertThrows(ClassNotFoundException.class, () ->
                Class.forName("cn.stylefeng.guns.generator.modular.controller.CodeController",
                        false, classLoader));
        assertThrows(ClassNotFoundException.class, () ->
                Class.forName("com.alibaba.fastjson.JSON", false, classLoader));
        assertThrows(ClassNotFoundException.class, () ->
                Class.forName("com.baomidou.mybatisplus.generator.AutoGenerator",
                        false, classLoader));
        assertThrows(ClassNotFoundException.class, () ->
                Class.forName("okhttp3.OkHttpClient", false, classLoader));
        assertThrows(ClassNotFoundException.class, () ->
                Class.forName("com.google.gson.Gson", false, classLoader));
    }
}
