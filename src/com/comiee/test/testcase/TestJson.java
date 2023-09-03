package com.comiee.test.testcase;

import com.comiee.mei.communal.JsonTool;
import com.comiee.test.comm.TestCase;
import com.google.gson.JsonObject;

import java.util.List;

public class TestJson extends TestCase {
    private final JsonObject jsonObject = JsonTool.createObject(
            "cmd", "test",
            "value", JsonTool.createObject(
                    "array", new int[]{1, 2, 3},
                    "list", List.of(
                            JsonTool.createObject(
                                    "jsonArray", JsonTool.createArray(4, 5, 6),
                                    "int", 7,
                                    "float", 8.9
                            ),
                            JsonTool.createObject(
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
                    "\"jsonArray\":[4,5,6]," +
                    "\"int\":7," +
                    "\"float\":8.9" +
                    "},{" +
                    "\"boolean\":true," +
                    "\"null\":null," +
                    "\"string\":\"{,}\"}" +
                    "]}}";

    private void testJsonDump() {
        assertEqual(string, JsonTool.dump(jsonObject));
    }

    private void testJsonParse() {
        assertEqual(jsonObject, JsonTool.parse(string));
    }

    public static void main(String[] args) {
        var testCase = new TestJson();
        testCase.testJsonDump();
        testCase.testJsonParse();

        System.out.println("测试通过！");
    }
}
