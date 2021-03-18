package my.than.lagana.core.lagana.model;

import my.than.lagana.core.bake.filters.LaganaFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchRequest {
    public int page;
    public int pageSize;
    public Map<String,String> select = new HashMap<>();
    public List<LaganaFilter> filters = new ArrayList<>();
}
