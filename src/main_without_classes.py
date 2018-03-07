from math import sqrt

# constant for the data file
FILE_PATH = 'src/active-businesses.tsv'

# constants for column indices
FORMAL_NAME_INDEX = 0
INFORMAL_NAME_INDEX = 1
ADDRESS_INDEX = 2
CITY_INDEX = 3
ZIP_CODE_INDEX = 4
CATEGORY_INDEX = 5
START_DATE_INDEX = 6
LATITUDE_INDEX = 7
LONGITUDE_INDEX = 8

# Oxy's coordinates
OXY_LATITUDE = 34.126813
OXY_LONGITUDE = -118.211904

# 1 degree longitude is about 57 miles (at 34 latitude)
DEGREES_TO_MILES = 57


def read_businesses():
    """Reads the data file.

    Returns:
        list of list of strings: A list of businesses, each a list of strings.
    """
    businesses = []
    lines = read_lines()
    line_index = 0
    while line_index < len(lines):
        line = lines[line_index]
        business = line_to_business(line)
        businesses.append(business)
        line_index += 1
    return businesses


def read_lines():
    """Reads data file into a list of strings.

    Returns:
        list of strings: List of lines in the data file.
    """
    lines = []
    with open(FILE_PATH) as fd:
        for line in fd.readlines():
            lines.append(line.strip())
    return lines


def line_to_business(line):
    """Converts a line of data into multiple fields.

    A line of data contains the following fields, separated by tabs:
    * formal name
    * informal name
    * address
    * city
    * zip code
    * category
    * start date
    * latitude
    * longitude

    Arguments:
        line (str): A line from the data file.

    Returns:
        list of strings: The different fields, as strings.
    """
    return line.split('\t')


def distance_from(business, latitude, longitude):
    """Calculates the distance to a coordinate.

    This function assumes that the latitude remains around 34 degrees.

    Arguments:
        business (list of strings): The target business.
        latitude (float): The latitude of the destination.
        longitude (float): The longitude of the destination.

    Returns:
        float: The distance from Oxy, in miles.
    """
    biz_lat = get_latitude(business)
    biz_long = get_longitude(business)
    lat_diff = biz_lat - latitude
    long_diff = biz_long - longitude
    coord_dist = sqrt( (lat_diff * lat_diff) + (long_diff * long_diff) )
    return coord_dist * DEGREES_TO_MILES


def get_latitude(business):
    """Extracts the latitude from a business.

    Arguments:
        business (list of strings): The target business.

    Returns:
        float: The latitude of the business.
    """
    return float(business[LATITUDE_INDEX])


def get_longitude(business):
    """Extracts the longitude from a business.

    Arguments:
        business (list of strings): The target business.

    Returns:
        float: The longitude of the business.
    """
    return float(business[LONGITUDE_INDEX])


def is_restaurant(business):
    """Determines if a business is categorized as a restaurant.

    Arguments:
        business (list of strings): The target business.

    Returns:
        bool: True if the business is a restaurant, False otherwise.
    """
    return business[CATEGORY_INDEX] == 'Full-service restaurants'


def is_restaurant_near_oxy(business):
    """Determines if a business is a restaurant within a mile of Oxy.

    Arguments:
        business (list of strings): The target business.

    Returns:
        bool: True if the business is a restaurant near Oxy, False otherwise.
    """
    latitude = get_latitude(business)
    longitude = get_longitude(business)
    distance = distance_from(business, OXY_LATITUDE, OXY_LONGITUDE)
    return (is_restaurant(business) and distance < 1)


def print_business_name(business):
    """Prints the name of a business.

    Use the informal name if it exists, fall back to the formal name if it doesn't.

    Arguments:
        business (list of strings): The target business.
    """
    if business[INFORMAL_NAME_INDEX] == '':
        print(business[FORMAL_NAME_INDEX])
    else:
        print(business[INFORMAL_NAME_INDEX])


def main():
    """Print the names of restaurants near Oxy 
    """
    businesses = read_businesses()
    biz_index = 0
    while biz_index < len(businesses):
        business = businesses[biz_index]
        if is_restaurant_near_oxy(business):
            print_business_name(business)
        biz_index += 1


if __name__ == '__main__':
    main()
