package de.hpi.semrecsys.base;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

public class JsonProduct {
    private ListMultimap<CsvKey, String> values = LinkedListMultimap.create();

    protected String getId(){
        return values.get(CsvKey.UNIQUE_KEY).iterator().next();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        JsonProduct that = (JsonProduct) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }

    public ListMultimap<CsvKey, String> getValues() {
        return values;
    }

    public void setValues(ListMultimap<CsvKey, String> values) {
        this.values = values;
    }
}
