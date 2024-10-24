package se.fk;

import io.quarkiverse.cucumber.CucumberQuarkusTest;
import io.quarkus.test.junit.QuarkusTest;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@QuarkusTest
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue = "se.fk.stepdefinitions"
)
public class CucumberTestRunner extends CucumberQuarkusTest {
}
