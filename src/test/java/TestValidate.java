/*
 * Copyright (C) 2022 Teclib'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.idmefv2.IDMEFException;
import org.idmefv2.IDMEFObject;
import org.idmefv2.IDMEFValidator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestValidate {

    private static String[] validate(IDMEFObject idmefObject) {
        IDMEFValidator validator = new IDMEFValidator();

        String[] ret = {};
        try {
            ret = validator.validate(idmefObject);
        } catch (IDMEFException e) {
            fail(e.getMessage());
        }

        for (String error: ret)
            fail(error);

        return ret;
    }

    private static String[] validate(byte[] json) {
        IDMEFValidator validator = new IDMEFValidator();

        String[] ret = {};
        try {
            ret = validator.validate(json);
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }

        for (String error: ret)
            fail(error);

        return ret;
    }

    @Test
    void testValidateMessage1() {
        assertEquals(validate(Util.message1()).length, 0);
    }

    @Test
    void testValidateString1() {
        assertEquals(validate(Util.string1().getBytes(StandardCharsets.UTF_8)).length, 0);
    }

    @Test
    void testValidateMessage2() {
        assertEquals(validate(Util.message2()).length, 0);
    }

    @Test
    void testValidateString2() {
        assertEquals(validate(Util.string2().getBytes(StandardCharsets.UTF_8)).length, 0);
    }
}
