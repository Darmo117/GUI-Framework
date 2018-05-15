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
    assertEquals(new BooleanTag("b1"), this.t1);
  }

  @Test
  public void testNotEqualsDifferentNamesSameTypes() {
    assertNotEquals(this.t2, this.t1);
  }

  @Test
  public void testNotEqualsSameNamesDifferentTypes() {
    assertNotEquals(this.t3, this.t1);
  }

  @Test
  public void testHashcodeEquals() {
    assertEquals(new BooleanTag("b1").hashCode(), this.t1.hashCode());
  }

  @Test
  public void testHashcodeNotEqualsDifferentNamesSameTypes() {
    assertNotEquals(this.t2.hashCode(), this.t1.hashCode());
  }

  @Test
  public void testHashcodeNotEqualsSameNamesDifferentTypes() {
    assertNotEquals(this.t3.hashCode(), this.t1.hashCode());
  }
}
