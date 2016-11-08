package com.r9software.infinitescrolling.util;

/**
 * Created by asus on 06/11/2016.
 */

public class Constants {
    public static final String QUERY = "query={" +
            " album(id: \"YWxidW06NThkY2ZkYjgtYjZhNC00M2ZiLWEyMGUtMmZhYjhiNGI1NDQ3\") {\n" +
            "    id\n" +
            "    name\n" +
            "    photos {\n" +
            "      records {\n" +
            "        urls {\n" +
            "          size_code\n" +
            "          url\n" +
            "          width\n" +
            "          height\n" +
            "          quality\n" +
            "          mime\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}\n";
    public static final String BASE_URL_API =  "https://core-graphql.dev.waldo.photos/gql?" ;
    public static final String COOKIE_VALUE = "__dev.waldo.auth__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiZTBmYWRkODQtNGE3Ny00MzZkLWE0MmUtNWRmNzFlNTJlNTYxIiwicm9sZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJpc3MiOiJ3YWxkbzpjb3JlIiwiZ3JhbnRzIjpbImFsYnVtczpkZWxldGU6KiIsImFsYnVtczpjcmVhdGU6KiIsImFsYnVtczplZGl0OioiLCJhbGJ1bXM6dmlldzoqIl0sImV4cCI6MTQ4MTE1OTgyMiwiaWF0IjoxNDc4NTY3ODIyfQ.inLIl3VqTM3CUzZ6rF4cBROTIOv4STPxxns43stNmJo; __dev.waldo.auth__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiZTBmYWRkODQtNGE3Ny00MzZkLWE0MmUtNWRmNzFlNTJlNTYxIiwicm9sZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJpc3MiOiJ3YWxkbzpjb3JlIiwiZ3JhbnRzIjpbImFsYnVtczpkZWxldGU6KiIsImFsYnVtczpjcmVhdGU6KiIsImFsYnVtczplZGl0OioiLCJhbGJ1bXM6dmlldzoqIl0sImV4cCI6MTQ4MDk5MzQ1MywiaWF0IjoxNDc4NDAxNDUzfQ.AxYbPEtDtlTeSgS2MQMe_vK_1mXoLR6DkWmr-E9CXHU";

}
