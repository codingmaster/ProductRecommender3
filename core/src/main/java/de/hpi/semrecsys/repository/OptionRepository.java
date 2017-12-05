package de.hpi.semrecsys.repository;

import de.hpi.semrecsys.OptionTable;

public interface OptionRepository extends BaseRepository<OptionTable>{

    OptionTable findByAttributeTableIdAndValue(int attributeId, String value);
}
