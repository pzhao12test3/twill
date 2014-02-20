package org.apache.twill.internal.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.apache.twill.api.ResourceSpecification;
import org.apache.twill.internal.DefaultResourceSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.util.Arrays;

/**
 * Maybe this checkstyle rule needs to be removed
 */
public class ResourceSpecificationCodecTest {
  private final Gson gson = new GsonBuilder().serializeNulls()
          .registerTypeAdapter(ResourceSpecification.class, new ResourceSpecificationCodec())
          .registerTypeAdapter(DefaultResourceSpecification.class, new ResourceSpecificationCodec())
          .create();

  @Test
  public void testCodec() throws Exception {
    String expectedString =
            "{" +
                    "\"cores\":2," +
                    "\"memorySize\":1024," +
                    "\"instances\":2," +
                    "\"uplink\":100," +
                    "\"downlink\":100," +
                    "\"hosts\":[\"one1\",\"two2\"]," +
                    "\"racks\":[\"three3\"]" +
            "}";
    final ResourceSpecification expected =
            new DefaultResourceSpecification(2, 1024, 2, 100, 100,
                    Arrays.asList("one1", "two2"), Arrays.asList("three3"));
    final String actualString = gson.toJson(expected);
    Assert.assertEquals(expectedString, actualString);

    final JsonElement expectedJson = gson.toJsonTree(expected);
    final ResourceSpecification actual = gson.fromJson(expectedJson, DefaultResourceSpecification.class);
    final JsonElement actualJson = gson.toJsonTree(actual);

    Assert.assertEquals(expectedJson, actualJson);
    ReflectionAssert.assertLenientEquals(expected, actual);
  }

  @Test
  public void testBuilder() throws Exception {
    final ResourceSpecification actual = ResourceSpecification.Builder.with()
            .setVirtualCores(5)
            .setMemory(4, ResourceSpecification.SizeUnit.GIGA)
            .setInstances(3)
            .setUplink(10, ResourceSpecification.SizeUnit.GIGA)
            .setDownlink(5, ResourceSpecification.SizeUnit.GIGA)
            .setHosts("a1", "b2", "c3")
            .setRacks("r2")
            .build();
    final DefaultResourceSpecification expected =
            new DefaultResourceSpecification(5, 4096, 3, 10240, 5120,
                    Arrays.asList("a1", "b2", "c3"), Arrays.asList("r2"));
    ReflectionAssert.assertLenientEquals(expected, actual);
  }

  @Test
  public void testBuilderWithLists() throws Exception {
    final ResourceSpecification actual = ResourceSpecification.Builder.with()
            .setVirtualCores(5)
            .setMemory(4, ResourceSpecification.SizeUnit.GIGA)
            .setInstances(3)
            .setUplink(10, ResourceSpecification.SizeUnit.GIGA)
            .setDownlink(5, ResourceSpecification.SizeUnit.GIGA)
            .setHosts(Arrays.asList("a1", "b2", "c3"))
            .setRacks(Arrays.asList("r2"))
            .build();
    final DefaultResourceSpecification expected =
            new DefaultResourceSpecification(5, 4096, 3, 10240, 5120,
                    Arrays.asList("a1", "b2", "c3"), Arrays.asList("r2"));
    ReflectionAssert.assertLenientEquals(expected, actual);
  }

}