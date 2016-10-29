import com.github.brunodles.githubpopular.api.Api;
import com.github.brunodles.githubpopular.api.GithubEndpoint;

import java.io.File;

/**
 * Created by bruno on 29/10/16.
 */

public class CheckGithubApi {
    public static void main(String[] args) {
        File tmp = new File("tmp");
        GithubEndpoint api = new Api("https://api.github.com", tmp).github();
        api.searchRepositories("language:Java", "stars", 1)
                .toBlocking()
                .subscribe(e -> System.out.println(e.items.get(0).full_name),
                        e -> e.printStackTrace(System.err));

        boolean delete = tmp.delete();
        if (!delete) System.out.println("Failed to delete tmp dir");
    }
}
