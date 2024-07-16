ALTER TABLE group_tbl
    CHANGE COLUMN description color enum ('labelRed', 'labelPink', 'labelOrange', 'labelYellow', 'labelGreen', 'labelLeaf', 'labelBlue', 'labelSky', 'labelNavy', 'labelIndigo', 'labelPurple', 'labelLavender', 'labelCharcoal', 'labelBrown') NOT NULL;
