package com.mentorassist.notification.util;

import static com.mentorassist.notification.util.Constants.DELIMITER;
import static com.mentorassist.notification.util.Constants.USER;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DbCompositeAttributeBuilder {

  private DbCompositeAttributeBuilder() {
  }

  public static AttributeValue getCallerIdAttributeValue(String value) {
    return getCompositeAttributeValueS(USER, value);
  }

  public static AttributeValue getCompositeAttributeValueS(String prefixValue, String value) {
    return getAttributeValueS(joinValues(prefixValue, value));
  }

  public static AttributeValue getAttributeValueS(String value) {
    return AttributeValue.builder().s(value).build();
  }

  public static AttributeValue getAttributeValueN(String value) {
    return AttributeValue.builder().n(value).build();
  }

  private static String joinValues(String prefix, String suffix) {
    return String.join(DELIMITER, prefix, suffix);
  }
}
