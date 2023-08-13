package com.comiee.test.testcase;

import com.comiee.mei.communal.Json;
import com.comiee.test.comm.TestCase;

import java.util.List;

public class TestJson extends TestCase {
    private final Json json = Json.of(
            "cmd", "test",
            "value", Json.of(
                    "array", new int[]{1, 2, 3},
                    "list", List.of(
                            Json.of(
                                    "list", List.of(4, 5, 6),
                                    "int", 7,
                                    "float", 8.9
                            ),
                            Json.of(
                                    "boolean", true,
                                    "null", null,
                                    "string", "{,}"
                            )
                    )
            )
    );
    private final String string =
            "{\"cmd\":\"test\",\"value\":{" +
                    "\"array\":[1,2,3]," +
                    "\"list\":[{" +
                    "\"list\":[4,5,6]," +
                    "\"int\":7," +
                    "\"float\":8.9" +
                    "},{" +
                    "\"boolean\":true," +
                    "\"null\":null," +
                    "\"string\":\"{,}\"}" +
                    "]}}";

    private void testJsonDump() {
        assertEqual(string, json.dump());
    }

    private void testJsonParse() {
        assertEqual(json, Json.parse(string));
    }

    public static void main(String[] args) {
        var testCase = new TestJson();
        testCase.testJsonDump();
        testCase.testJsonParse();

        System.out.println("测试通过！");
    }
}
