#!/usr/bin/env python3

from csv import reader

'''
column descriptions:
    0:  location account #
    1:  business name
    2:  dba name
    3:  street address
    4:  city
    5:  zip code
    6:  location description
    7:  mailing address
    8:  mailing city
    9:  mailing zip code
    10: naics
    11: primary naics description
    12: council district
    13: location start date
    14: location end date
    15: location
'''

def get_precise_coordinates(row):
    """Determine if a row has precise coordinates (4+ decimal places).

    Arguments:
        row ([str]): A row from the raw data.

    Returns:
        [str, str] or None: The coordinates if they are precise, None otherwise
    """
    coord = row[15]
    if ', ' not in coord:
        return None
    latitude, longitude = coord.strip('()').split(',')
    if not all(('.' in dim and len(dim) - dim.index('.') > 4) for dim in [latitude, longitude]):
        return None
    return [latitude, longitude]


def main():
    with open('active-businesses-raw.csv') as read_fd:
        # ignore the first line of headings
        read_fd.readline()
        with open('active-businesses.tsv', 'w') as write_fd:
            for row in reader(read_fd):
                # only include businesses with precise coordinates
                coords = get_precise_coordinates(row)
                if coords is None:
                    continue
                # collect all relevant values
                values = [row[col_index] for col_index in [1, 2, 3, 4, 5, 11, 13]]
                values.extend(coords)
                # write to the new file in a nicer format
                write_fd.write('\t'.join(values))
                write_fd.write('\n')

if __name__ == '__main__':
    main()
