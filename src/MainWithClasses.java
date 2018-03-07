import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

public class MainWithClasses {

    /* constant for the data file */
    public static String FILE_PATH = "src/active-businesses.tsv";

    /* constants for column indices */
    public static int FORMAL_NAME_INDEX = 0;
    public static int INFORMAL_NAME_INDEX = 1;
    public static int ADDRESS_INDEX = 2;
    public static int CITY_INDEX = 3;
    public static int ZIP_CODE_INDEX = 4;
    public static int CATEGORY_INDEX = 5;
    public static int START_DATE_INDEX = 6;
    public static int LATITUDE_INDEX = 7;
    public static int LONGITUDE_INDEX = 8;

    /* Oxy's coordinates */
    public static double OXY_LATITUDE = 34.126813;
    public static double OXY_LONGITUDE = -118.211904;

    /* 1 degree longitude is about 57 miles (at 34 latitude) */
    public static double DEGREES_TO_MILES = 57;

    /**
     * Reads the data file
     *
     * @return     a list of businesses, each a list of strings
     */
    public static ArrayList<Business> read_businesses() {
        ArrayList<Business> businesses = new ArrayList<Business>();
        ArrayList<String> lines = read_lines();
        int i = 0;
        while (i < lines.size()) {
            String line = lines.get(i);
            Business business = line_to_business(lines.get(i));
            businesses.add(business);
            i ++;
        }
        return businesses;
    }

    /**
     * Reads data file into a list of strings.
     *
     * @return an ArrayList of strings, each a line in the data file
     */
    public static ArrayList<String> read_lines() {
        ArrayList<String> lines = new ArrayList<String>();
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Working Directory is " + System.getProperty("user.dir") + ".");
            System.out.println("Cannot find '" + FILE_PATH + "'; quitting.");
            System.exit(1);
        }
        return lines;
    }

    /**
     * Converts a line of data into multiple fields
     *
     * @param line a line from the data file
     * @return     the different fields, as strings
     */
    public static Business line_to_business(String line) {
        // this creates a list of strings from a single line of the data file
        // each string corresponds to a field - formal name, informal name, address, etc.
        ArrayList<String> fields = new ArrayList<String>(Arrays.asList(line.split("\t")));
        Business restaurant = new Business();
        restaurant.formal_name = fields.get(FORMAL_NAME_INDEX);
        restaurant.informal_name = fields.get(INFORMAL_NAME_INDEX);
        restaurant.address = fields.get(ADDRESS_INDEX);
        restaurant.city = fields.get(CITY_INDEX);
        restaurant.zip_code = fields.get(ZIP_CODE_INDEX);
        restaurant.category = fields.get(CATEGORY_INDEX);
        restaurant.start_date = fields.get(START_DATE_INDEX);
        restaurant.latitude = Double.parseDouble(fields.get(LATITUDE_INDEX));
        restaurant.longitude = Double.parseDouble(fields.get(LONGITUDE_INDEX));
        return restaurant;
    }

    /**
     * Calculates the distance of a coordinate from Oxy
     *
     * @param business the target business
     * @param latitude  the latitude of the destination
     * @param longitude the longitude of the destination
     * @return          the distance from Oxy, in miles
     */
    public static double distance_from(Business business, double latitude, double longitude) {
    double biz_lat = business.latitude;
    double biz_long = business.longitude;
    double lat_diff = biz_lat - latitude;
    double long_diff = biz_long - longitude;
    double coord_dist = java.lang.Math.sqrt((lat_diff * lat_diff) + (long_diff * long_diff));
    return coord_dist * DEGREES_TO_MILES;
    }
    /**
     * Extracts the latitude from a business
     *
     * @param business the target business
     * @return         the latitude of the business
     */
    public static double get_latitude(Business business) {
        double lat = business.latitude;
        return lat;
    }

    /**
     * Extracts the longitude from a business
     *
     * @param business the target business
     * @return         the longitude of the business
     */
    public static double get_longitude(Business business) {

        double lon = business.longitude;
        return lon;
    }

    /**
     * Determines if a business is categorized as a restaurant
     *
     * @param business the target business
     * @return         true if the business is a restaurant, false otherwise
     */
    public static boolean is_restaurant(Business business) {
        // note: to compare Strings, DO NOT use ==
        // instead, use the .equals method
        // For example, DO NOT DO
        //
        //     if ("hello" == "world") { ... }
        //
        // Instead, do
        //
        //     if ("hello".equals("world")) { ... }
        String Businessname = business.category;
        return Businessname.equals("Full-service restaurants");
    }

    /**
     * Determines if a business is a restaurant within a mile of Oxy
     *
     * @param business the target business
     * @return         true if the business is a restaurant near Oxy, false otherwise
     */
    public static boolean is_restaurant_near_oxy(Business business) {
        double latitude = get_latitude(business);
        double longitude = get_longitude(business);
        double distance = distance_from(business, OXY_LATITUDE, OXY_LONGITUDE);
        return (is_restaurant(business) && distance < 1);
    }

    /**
     * Prints the name of a business
     *
     * @param business the target business
     */
    public static void print_business_name(Business business) {
        // see note in is_restaurant about comparing Strings
        if (business.informal_name.equals("")){
            System.out.println(business.formal_name);
        }else if (!business.informal_name.equals("")) {
            System.out.println(business.informal_name);}
    }

    /**
     * Print the names of restaurants near Oxy
     */
    public static void main(String[] args) {
        ArrayList<Business> businesses = read_businesses();
        int biz_index = 0;
        while (biz_index < businesses.size()) {
            Business business = businesses.get(biz_index);
            if (is_restaurant_near_oxy(business)) {
                print_business_name(business);
            }
            biz_index ++;
        }
    }
}