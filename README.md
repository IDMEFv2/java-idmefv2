# Java IDMEFv2 library

![GitHub top language](https://img.shields.io/github/languages/top/idmefv2/java-idmefv2)
![GitHub](https://img.shields.io/github/license/idmefv2/java-idmefv2)
![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/idmefv2/java-idmefv2)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/idmefv2/java-idmefv2)
![GitHub issues](https://img.shields.io/github/issues/idmefv2/java-idmefv2)

A Java library for parsing, handling, and generating JSON IDMEFv2 messages. It can be used to represent Incident Detection Message Exchange Format (IDMEFv2) messages in memory, validate them and serialize/unserialize them for exchange with other systems.

IDMEFv2 messages can be transported using the [`java-idmef-transport-library`](https://github.com/idmefv2/java-idmef-transport-library).

This code is currently in an experimental status and is regularly kept in sync with the development status of the IDMEFv2 format.

The latest revision of the IDMEFv2 format specification can be found there: https://github.com/IDMEFv2/IDMEFv2-Drafts

You can find more information about the previous version (v1) of the Intrusion Detection Message Exchange Format in [RFC 4765](https://tools.ietf.org/html/rfc4765).

## Compiling the library

The following prerequisites must be installed on your system to install and use this library:

* Java: version 11 or above

The library has the following third-party dependencies:

* Jackson (aka JSON for Java): https://github.com/FasterXML/jackson
* Networknt Java JSON Schema Validator: https://github.com/networknt/json-schema-validator

**Note**: building automaticaly pulls the needed dependencies.

To compile the library:

``` shell
mvn package
```

This will build a JAR archive located in `./target`.

## Using the library

### Add the library JAR to your project dependencies

After downloading the library JAR from https://github.com/IDMEFv2/java-idmefv2/releases, add the JAR file to your project:

* **maven**: see https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html
* **gradle**: see for instance https://www.baeldung.com/gradle-dependencies-local-jar

### Message creation

A new message can be created by instantiating the `org.idmefv2.IDMEFObject` class. Once created, message fields can be set using the `put()` method.

``` java
import org.idmefv2.IDMEFObject;

class Test {
    static IDMEFObject message1() {
        IDMEFObject msg = new IDMEFObject();
        msg.put("Version", "2.D.V05");
        msg.put("ID", "09db946e-673e-49af-b4b2-a8cd9da58de6");
        msg.put("CreateTime", "2021-11-22T14:42:51.881033Z");

        IDMEFObject analyzer = new IDMEFObject();
        analyzer.put("IP", "127.0.0.1");
        analyzer.put("Name", "foobar");
        analyzer.put("Model", "generic");
        analyzer.put("Category", new String[]{"LOG"});
        analyzer.put("Data", new String[]{"Log"});
        analyzer.put("Method", new String[]{"Monitor"});

        msg.put("Analyzer", analyzer);

        return msg;
    }

    public static void main(String[] args)
    {
        IDMEFObject msg1 = message1();

    	System.out.println(msg1.get("ID"));
    }
}
```

### Message validation

You can validate an IDMEFv2 message using `validate()` method of class `IDMEFValidator`. A `IDMEFException` is raised if the message is invalid.

``` java
import org.idmefv2.IDMEFException;
import org.idmefv2.IDMEFObject;
import org.idmefv2.IDMEFValidator;

/* see above to generate IDMEF message */

IDMEFObject msg1 = message1();

IDMEFValidator validator = new IDMEFValidator();

try {
    validator.validate(msg1);
} catch (IDMEFException e) {
    System.err.println(e.getMessage());
}

System.out.println("Message is valid");
```

### Message serialization

Before the message can be sent to a remote system, it must be serialized using the `serialize()` method.

``` java
import org.idmefv2.IDMEFObject;

/* see above to generate IDMEF message */

IDMEFObject msg1 = message1();

byte[] b = null;

try {
    b = msg1.serialize();
} catch (Exception e) {
    System.err.println(e.getMessage());
}

System.out.println("Message is serialized:" + new String(b));
```

### Message deserialization

Likewise, when a message is received in its serialized form, it must be first deserialized using the `deserialize()` class method.

``` java
import org.idmefv2.IDMEFObject;

class Test4 {
    static String string1() {
        return "{\n" +
                "\"Version\":\"2.D.V05\",\n" +
                "\"CreateTime\":\"2021-11-22T14:42:51.881033Z\",\n" +
                "\"ID\":\"09db946e-673e-49af-b4b2-a8cd9da58de6\",\n" +
                "\"Analyzer\":{\n" +
                "\"Category\":[\"LOG\"],\n" +
                "\"IP\":\"127.0.0.1\",\n" +
                "\"Model\":\"generic\",\n" +
                "\"Data\":[\"Log\"],\n" +
                "\"Method\":[\"Monitor\"],\n" +
                "\"Name\":\"foobar\"\n" +
                "}\n" +
                "}\n";
    }

    public static void main(String[] args)
    {
    	String json = string1();

        IDMEFObject msg = null;

        try {
            msg = IDMEFObject.deserialize(json.getBytes());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    	System.out.println("Message is deserialized and its ID is " + msg.get("ID"));
    }
}
```

## Contributions

All contributions must be licensed under the Apache-2.0 license. See the LICENSE file inside this repository for more information.
