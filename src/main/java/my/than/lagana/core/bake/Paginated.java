package my.than.lagana.core.bake;

import java.util.ArrayList;
import java.util.List;

public class Paginated<T> {
    public int page;
    public int pageSize;
    public int totalPages;
    public int totalResults;

    public List<T> results=new ArrayList<>();
}
