import ecs100.*;

/** Program for calculating carbon emissions */

public class CarbonEmissionsCalculator{

    // Constants for emission factors
    public static final double EMISSION_FACTOR_POWER = 0.0977;              // Emissions factor of electricity per kWh
    public static final double EMISSION_FACTOR_WASTE = 0.299;               // Emissions factor of food waste per kg
    public static final double ANNUAL_AVERAGE_POWER_EMISSION_NZ = 266.5;    // Annual average carbon emissions in NZ (kg) from electricity use
    public static final double ANNUAL_AVERAGE_FOOD_WASTE_EMISSION_NZ = 18;  // Annual average carbon emissions in NZ (kg) from food waste


    /**
     * Calculates and prints carbon emissions
     */
    public void calculateEmissions(){
        // Display header
        UI.println("----------------------------------");
        UI.println("Carbon Emission Calculator");

        // Get user input for electricity and food waste
        double electricityUsed = UI.askDouble("Enter monthly household electricity consumption in kWh: ");
        double foodWaste = UI.askDouble("Enter weekly household food waste in kg: ");

        // Calculate emission from electricity and food waste
        double monthlyPowerEmission = electricityUsed * EMISSION_FACTOR_POWER;
        double weeklyFoodEmission = foodWaste * EMISSION_FACTOR_WASTE;

        // Calculate daily emissions
        double dailyFoodEmission = weeklyFoodEmission / 7;
        double dailyPowerEmission = monthlyPowerEmission / 31;

        // Calculate total daily average emissions
        double totalDailyAverageEmission = dailyPowerEmission + dailyFoodEmission;

        // Display result for electricity and food waste emissions
        UI.printf("CO2 emissions caused by electricity (kg CO2-e) in the month: %.2f\n", monthlyPowerEmission);
        UI.printf("CO2 emissions caused by waste (kg CO2-e) in the week: %.2f\n", weeklyFoodEmission);
        UI.printf("Daily average CO2 emissions (kg CO2-e): %.2f\n", totalDailyAverageEmission);

        // Get user input for household size
        double householdSize = UI.askDouble("Enter the number of individuals in the household: ");

        // Calculate emissions per person and annual per person emissions
        double emissionPerPerson = totalDailyAverageEmission / householdSize;
        double annualPerPersonEmission = emissionPerPerson * 365;

        // Calculate New Zealand's total annual emissions
        double NZTotalAnnualEmission = ANNUAL_AVERAGE_POWER_EMISSION_NZ + ANNUAL_AVERAGE_FOOD_WASTE_EMISSION_NZ;

        // Calculate and display the percentage of user's emissions compared to NZ's annual average
        double percentage = (annualPerPersonEmission / NZTotalAnnualEmission) * 100;
        UI.printf("You emit %.2f CO2 emissions of NZ annual average\n", percentage);

        // Display footer
        UI.println("----------------------------------");
    }

    /**
     * Calculates and prints car emissions
     */
    public void carEmissionCalculation(){
        // Local constants for car emission factors
        double EMISSION_FACTOR_PETROL = 0.211;
        double EMISSION_FACTOR_ELECTRIC = 0.023;

        // Get user input for weekly car distance
        double weeklyCarDriven = UI.askDouble("How many KM do you drive weekly: ");

        // Calculate emissions for petrol and electric cars
        double petrolWeeklyEmission = weeklyCarDriven * EMISSION_FACTOR_PETROL;
        double electricWeeklyEmission = weeklyCarDriven * EMISSION_FACTOR_ELECTRIC;

        // Display result for car emissions
        UI.println("----------------------------------");
        UI.printf("CO2 emissions caused by petrol car usage (kg CO2-e) in the week: %.2f\n", petrolWeeklyEmission);
        UI.printf("CO2 emissions caused by electric car usage (kg CO2-e) in the week: %.2f\n", electricWeeklyEmission);

        // Calculate and display the percentage difference between electric and petrol car emission
        double percentage = (electricWeeklyEmission / petrolWeeklyEmission) * 100;
        UI.printf("You emit %.2f more CO2 then electric cars.\n", percentage);
        UI.println("----------------------------------");
    }

    /**
     * Sets up the GUI
     */
    public void setupGUI(){
        UI.initialise();
        UI.addButton("Calculate Emissions", this::calculateEmissions);
        UI.addButton("Calculate Car Emissions", this::carEmissionCalculation);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1);    // Expand the Text pane
    }

    public static void main(String[] args){
        CarbonEmissionsCalculator cec = new CarbonEmissionsCalculator();
        cec.setupGUI();
    }

}
