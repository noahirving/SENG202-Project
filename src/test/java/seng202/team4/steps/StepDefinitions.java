package seng202.team4.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import seng202.team4.model.Calculations;
import seng202.team4.model.Route;

public class StepDefinitions {
    Route route;

    @Given("there is a new Route")
    public void thereIsANewRoute() {
        route = new Route();
    }
    @When("I set the distance to {double}")
    public void iSetTheDistanceTo(double distance) {
        route.setDistance(distance);
    }
    @Then("the calculated carbon emissions should be {double}")
    public void theCalculatedCarbonEmissionsShouldBe(Double carbonEmissions) {
        Assert.assertEquals(carbonEmissions, Calculations.calculateEmissions(route), 0.001);
    }
}
