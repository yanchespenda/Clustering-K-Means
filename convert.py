import csv
writer = csv.writer(open("data_export.csv", "wt"), quoting=csv.QUOTE_NONE)
reader = csv.reader(open("data_export--5cdfaec379325.csv", "rt"), skipinitialspace=True)
writer.writerows(reader)