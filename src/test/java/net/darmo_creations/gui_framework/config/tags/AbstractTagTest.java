package net.darmo_creations.gui_framework.config.tags;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AbstractTagTest {
  private BooleanTag t1, t2;
  private IntegerTag t3;

  @Before
  public void setUp() {
    this.t1 = new BooleanTag("b1");
    this.t2 = new BooleanTag("b2");
    this.t3 = new IntegerTag("b1");
  }

  @Test
  public void testEquals() {
    assertTrue(this.t1.equals(new BooleanTag("b1")));
  }

  @Test
  public void testNotEqualsDifferentNamesSameTypes() {
    assertFalse(this.t1.equals(this.t2));
  }

  @Test
  public void testNotEqualsSameNamesDifferentTypes() {
    assertFalse(this.t1.equals(this.t3));
  }

  @Test
  public void testHashcodeEquals() {
    assertTrue(this.t1.hashCode() == new BooleanTag("b1").hashCode());
  }

  @Test
  public void testHashcodeNotEqualsDifferentNamesSameTypes() {
    assertFalse(this.t1.hashCode() == this.t2.hashCode());
  }

  @Test
  public void testHashcodeNotEqualsSameNamesDifferentTypes() {
    assertFalse(this.t1.hashCode() == this.t3.hashCode());
  }
}
