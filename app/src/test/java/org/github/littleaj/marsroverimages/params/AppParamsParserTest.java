package org.github.littleaj.marsroverimages.params;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AppParamsParserTest {
  @Test
  void emptyParamsResultsInAllFieldsNull() {
    var args = new String[0];
    var result = AppParamsParser.parse(args);
    assertThat(result).isNotNull();
    assertThat(result.getApiKey()).isNull();
    assertThat(result.getLimitPerDay()).isNull();
    assertThat(result.getStartDate()).isNull();
  }

  @Test
  void emptyKeyParamSetsDate() {
    var args = new String[] {
      "2020-02-22"
    };
    var result = AppParamsParser.parse(args);
    assertThat(result).isNotNull();
    assertThat(result.getApiKey()).isNull();
    assertThat(result.getLimitPerDay()).isNull();
    assertThat(result.getStartDate()).isEqualTo("2020-02-22");
  }

  @Test
  void invlalidDateLeavesResultStartDateNull() {
    var args = new String[] {
      "20-20-02-22"
    };
    var result = AppParamsParser.parse(args);
    assertThat(result).isNotNull();
    assertThat(result.getApiKey()).isNull();
    assertThat(result.getLimitPerDay()).isNull();
    assertThat(result.getStartDate()).isNull();
  }

  @Test
  void apikeyOptionSetsApiKeyInResult() {
    var args = new String[] {
      "--apikey", "abc123"
    };
    var result = AppParamsParser.parse(args);
    assertThat(result).isNotNull();
    assertThat(result.getLimitPerDay()).isNull();
    assertThat(result.getStartDate()).isNull();
    assertThat(result.getApiKey()).isEqualTo("abc123");
  }

  @Test
  void emptyApikeyOptionLeavesApiKeyNullInResult() {
    var args = new String[] {
      "--apikey"
    };
    var result = AppParamsParser.parse(args);
    assertThat(result).isNotNull();
    assertThat(result.getLimitPerDay()).isNull();
    assertThat(result.getStartDate()).isNull();
    assertThat(result.getApiKey()).isNull();
  }
  
  @Test
  void emptyLimitOptionLeavesLimitNullInResult() {
    var args = new String[] {
      "--limitPerDay"
    };
    var result = AppParamsParser.parse(args);
    assertThat(result).isNotNull();
    assertThat(result.getLimitPerDay()).isNull();
    assertThat(result.getStartDate()).isNull();
    assertThat(result.getApiKey()).isNull();
  }

  @Test
  void limitOptionSetsLimitInResult() {
    var args = new String[] {
      "--limitPerDay", "whatever"
    };
    var result = AppParamsParser.parse(args);
    assertThat(result).isNotNull();
    assertThat(result.getLimitPerDay()).isEqualTo("whatever");
    assertThat(result.getStartDate()).isNull();
    assertThat(result.getApiKey()).isNull();
  }

  @Test
  void dateCanAppearInAnyOrder() {
    var args1 = new String[] {
      "--apikey", "testkey", "2021-12-31", "--limitPerDay", "50", "ignored"
    };
    var args2 = new String[] {
      "--apikey", "testkey", "--limitPerDay", "50", "ignored", "2021-12-31"
    };
    var args3 = new String[] {
      "2021-12-31", "--apikey", "testkey", "--limitPerDay", "50", "ignored"
    };

    var params1 = AppParamsParser.parse(args1);
    var params2 = AppParamsParser.parse(args2);
    var params3 = AppParamsParser.parse(args3);

    assertThat(params1).isNotNull();
    assertThat(params1.getApiKey()).isEqualTo("testkey");
    assertThat(params1.getLimitPerDay()).isEqualTo("50");
    assertThat(params1.getStartDate()).isEqualTo("2021-12-31");
    assertThat(params2).usingRecursiveComparison().isEqualTo(params1);
    assertThat(params3).usingRecursiveComparison().isEqualTo(params1);
  }
}
