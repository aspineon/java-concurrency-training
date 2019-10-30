package pl.training.concurrency.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.training.concurrency.search.github.Repository;
import pl.training.concurrency.search.wikipedia.Article;

import java.util.List;

@AllArgsConstructor
@Data
public class Results {

    private List<Repository> repositories;
    private List<Article> articles;

}
