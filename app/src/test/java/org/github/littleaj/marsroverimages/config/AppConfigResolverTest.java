package org.github.littleaj.marsroverimages.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.time.LocalDate;
import java.util.Properties;

import org.github.littleaj.marsroverimages.MissingApiKeyException;
import org.github.littleaj.marsroverimages.params.AppParams;
import org.github.littleaj.marsroverimages.params.ParamsTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppConfigResolverTest {
  
  AppConfig config;
  AppConfigResolver resolver;

  @BeforeEach
  void setup() {
    config = new AppConfig();
    resolver = new AppConfigResolver();
  }

  @Test
  void isNotBlankIsFalseWhenNullOrEmptyOrWhitespace() {
    assertThat(AppConfigResolver.isNotBlank(null)).isFalse();
    assertThat(AppConfigResolver.isNotBlank("")).isFalse();
    assertThat(AppConfigResolver.isNotBlank(" \n\t\f\r")).isFalse();
    assertThat(AppConfigResolver.isNotBlank("anything else")).isTrue();
  }

  @Test
  void resolveLimitSetsLimitInResult() {
    AppConfigResolver.resolveLimit("test", config, "123");
    assertThat(config.getLimitPerDay()).isEqualTo(123);
  }

  @Test
  void resolveApiKeySetsApiKeyInResult() {
    AppConfigResolver.resolveApiKey(config, "test-API-key");
    assertThat(config.getApiKey()).isEqualTo("test-API-key");
  }

  @Test
  void resolveStartDateSetsStartDateInResult() {
    AppConfigResolver.resolveStartDate("test", config, "2020-12-20");
    assertThat(config.getStartDate()).isEqualTo(LocalDate.parse("2020-12-20"));
  }

  @Test
  void resolveLimitWithInvalidLimitDoesNothing() {
    assumeThat(config.getLimitPerDay()).isEqualTo(AppConfig.DEFAULT_LIMIT_PER_DAY);
    
    AppConfigResolver.resolveLimit("test", config, "not valid");
    assertThat(config.getLimitPerDay()).isEqualTo(AppConfig.DEFAULT_LIMIT_PER_DAY);
    
    AppConfigResolver.resolveLimit("test", config, "");
    assertThat(config.getLimitPerDay()).isEqualTo(AppConfig.DEFAULT_LIMIT_PER_DAY);
    
    AppConfigResolver.resolveLimit("test", config, null);
    assertThat(config.getLimitPerDay()).isEqualTo(AppConfig.DEFAULT_LIMIT_PER_DAY);
  }

  @Test
  void resolveStartDateWithInvalidDateDoesNothing() {
    assumeThat(config.getStartDate()).isEqualTo(AppConfig.DEFAULT_START_DATE);
    
    AppConfigResolver.resolveStartDate("test", config, "not valid");
    assertThat(config.getStartDate()).isEqualTo(AppConfig.DEFAULT_START_DATE);
    
    AppConfigResolver.resolveStartDate("test", config, "2020-13-13");
    assertThat(config.getStartDate()).isEqualTo(AppConfig.DEFAULT_START_DATE);
    
    AppConfigResolver.resolveStartDate("test", config, "");
    assertThat(config.getStartDate()).isEqualTo(AppConfig.DEFAULT_START_DATE);
    
    AppConfigResolver.resolveStartDate("test", config, null);
    assertThat(config.getStartDate()).isEqualTo(AppConfig.DEFAULT_START_DATE);
  }

  @Test
  void resolveApiKeyWithInvalidKeyDoesNothing() {
    assumeThat(config.getApiKey()).isNull();
    
    AppConfigResolver.resolveApiKey(config, "");
    assumeThat(config.getApiKey()).isNull();
    
    AppConfigResolver.resolveApiKey(config, " \r\n\f\t");
    assumeThat(config.getApiKey()).isNull();
    
    AppConfigResolver.resolveApiKey(config, null);
    assumeThat(config.getApiKey()).isNull();
  }

  @Test
  void resolveWithoutApiKeyThrows() {
    assertThatThrownBy(() -> {
      resolver.resolve();
    }).hasMessageContaining("apikey")
      .isInstanceOf(MissingApiKeyException.class);
  }

  @Test
  void resolveWithParametersSetsFieldsInResult() throws MissingApiKeyException {
    AppConfig config = resolver.parameters(getTestParams()).resolve();
    assertThat(config.getApiKey()).isEqualTo("test-param-key");
    assertThat(config.getStartDate()).isEqualTo(LocalDate.parse("2020-02-02"));
    assertThat(config.getLimitPerDay()).isEqualTo(54321);
    assertThat(config.getDays()).isEqualTo(12);
  }

  @Test
  void resolveWithPropertiesSetsFieldsInResult() throws MissingApiKeyException {
    AppConfig config = resolver.properties(getTestProps()).resolve();
    assertThat(config.getApiKey()).isEqualTo("test-prop-key");
    assertThat(config.getStartDate()).isEqualTo(LocalDate.parse("2020-03-03"));
    assertThat(config.getLimitPerDay()).isEqualTo(12345);
    assertThat(config.getDays()).isEqualTo(1);
  }

  @Test
  void resolveWithPropsAndParamsFavorsParamsInResult() throws MissingApiKeyException {
    AppConfig config = resolver.parameters(getTestParams()).properties(getTestProps()).resolve();
    assertThat(config.getApiKey()).isEqualTo("test-param-key");
    assertThat(config.getStartDate()).isEqualTo(LocalDate.parse("2020-02-02"));
    assertThat(config.getLimitPerDay()).isEqualTo(54321);
    assertThat(config.getDays()).isEqualTo(12);
  }

  @Test
  void resolveWithParamsMissingApiKeyUsesPropsInResult() throws MissingApiKeyException {
    var params = ParamsTestUtils.newParams(null, "2020-04-04", "567", "32");
    AppConfig config = resolver.parameters(params).properties(getTestProps()).resolve();
    assertThat(config.getApiKey()).isEqualTo("test-prop-key");
    assertThat(config.getStartDate()).isEqualTo(LocalDate.parse("2020-04-04"));
    assertThat(config.getLimitPerDay()).isEqualTo(567);
    assertThat(config.getDays()).isEqualTo(32);
  }

  private Properties getTestProps() {
    var props = new Properties();
    props.setProperty("apikey", "test-prop-key");
    props.setProperty("startDate", "2020-03-03");
    props.setProperty("limitPerDay", "12345");
    props.setProperty("days", "1");
    return props;
  }

  private static AppParams getTestParams() {
    return ParamsTestUtils.newParams("test-param-key", "2020-02-02", "54321", "12");
  }
}
