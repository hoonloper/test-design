package com.bank;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {
  @Test
  public void 덧셈_연산을_할_수_있다() {
    long num1 = 2;
    String operator = "+";
    long num2 = 3;

    Calculator calculator = new Calculator();

    long result = calculator.calculate(num1, operator, num2);

    assertEquals(5, result);
    assertThat(result).isEqualTo(5);
  }

  @Test
  public void 뺄셈_연산을_할_수_있다() {
    long num1 = 2;
    String operator = "-";
    long num2 = 3;

    Calculator calculator = new Calculator();

    long result = calculator.calculate(num1, operator, num2);

    assertEquals(-1, result);
    assertThat(result).isEqualTo(-1);
  }

  @Test
  public void 곱셈_연산을_할_수_있다() {
    long num1 = 2;
    String operator = "*";
    long num2 = 3;

    Calculator calculator = new Calculator();

    long result = calculator.calculate(num1, operator, num2);

    assertEquals(6, result);
    assertThat(result).isEqualTo(6);
  }


  @Test
  public void 나눗셈_연산을_할_수_있다() {
    long num1 = 3;
    String operator = "/";
    long num2 = 3;

    Calculator calculator = new Calculator();

    long result = calculator.calculate(num1, operator, num2);

    assertEquals(1, result);
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void 잘못된_연산자가_요청으로_들어올_경우_에러가_난다() {
    long num1 = 2;
    String operator = "x";
    long num2 = 3;

    Calculator calculator = new Calculator();

    assertThrows(InvalidOperatorException.class, () -> {
      calculator.calculate(num1, operator, num2);
    });
  }
}
