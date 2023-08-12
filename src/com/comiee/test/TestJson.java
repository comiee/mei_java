package com.comiee.test;

import com.comiee.mei.communal.Json;

import java.util.List;

public class TestJson extends TestCase {
    private void testJsonDump() throws Exception {
        Json json = Json.of(
                "cmd", "test",
                "value", Json.of(
                        //"array", new int[]{1, 2, 3},
                        "list", List.of(4, 5, 6),
                        "int", 7,
                        "float", 8.9,
                        "boolean", true,
                        "null", Json.Null,
                        "string", "{}"
                )
        );
        assertEqual("{\"cmd\":\"test\",\"value\":{" +
                        "\"list\":[4,5,6]," +
                        "\"int\":7," +
                        "\"float\":8.9," +
                        "\"boolean\":true," +
                        "\"null\":null," +
                        "\"string\":\"{}\"}}",
                json.dump());
    }

    private void testJsonParse() {
        Json json = Json.of(
                "cmd", "test",
                "value", Json.of(
                        //"array", new int[]{1, 2, 3},
                        "list", List.of(4, 5, 6),
                        "int", 7,
                        "float", 8.9,
                        "boolean", true,
                        "null", Json.Null,
                        "string", "{}"
                )
        );
        assertEqual(json, Json.parse("{\"cmd\":\"test\",\"value\":{" +
                "\"list\":[4,5,6]," +
                "\"int\":7," +
                "\"float\":8.9," +
                "\"boolean\":true," +
                "\"null\":null," +
                "\"string\":\"{}\"}}"));
    }

    public static void main(String[] args) throws Exception {
        var testCase = new TestJson();
        testCase.testJsonDump();

        System.out.println("测试通过！");
    }
}
