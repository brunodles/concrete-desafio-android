import com.github.brunodles.githubpopular.api.Api;
import com.github.brunodles.githubpopular.api.GithubEndpoint;

import java.io.File;
import java.io.IOException;

/**
 * Created by bruno on 29/10/16.
 */

public class CheckGithubApi {

    public static final String CLIENT_iD = "a6727ed6f6e868550c17";
    public static final String CLIENT_SECRET = "76035bdf010ffc8ac7acb2adc02abd80caf6d96a";

    public static void main(String[] args) throws IOException {
        File tmp = File.createTempFile("tmp", ".tmp");
        GithubEndpoint api = new Api("https://api.github.com", tmp, () -> CLIENT_iD, () -> CLIENT_SECRET).github();
        api.searchRepositories("language:Java", "stars", 1)
                .toBlocking()
                .subscribe(e -> System.out.println(e.items.get(0).full_name),
                        e -> e.printStackTrace(System.err));
    }
}
