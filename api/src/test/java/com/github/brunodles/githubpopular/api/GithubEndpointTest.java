package com.github.brunodles.githubpopular.api;

import com.github.brunodles.githubpopular.api.dto.SearchEvenlope;
import com.github.brunodles.oleaster_suite_runner.OleasterSuiteRunner;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action0;
import rx.functions.Action1;

import static com.github.brunodles.testhelper.TestResourceHelper.resourceFile;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.after;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.before;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.beforeEach;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by bruno on 29/10/16.
 */
@RunWith(OleasterSuiteRunner.class)
public class GithubEndpointTest {

    private static final int WIREMOCK_PORT = 8089;
    private static final String API_URL = "http://localhost:" + WIREMOCK_PORT;
    public static final String CLIENT_ID = "myClientId";
    public static final String CLIENT_KEY = "myAwesomeSecret";

    private WireMockServer wireMockServer = new WireMockServer(
            wireMockConfig().port(WIREMOCK_PORT));
    private File folder;

    private GithubEndpoint githubEndpoint;

    private Action1 onNext;
    private Action1<Throwable> onError;
    private Action0 onComplete;

    {

        describe("Given a GithubEndpoint", () -> {

            before(() -> {
                WireMock.configureFor(WIREMOCK_PORT);
                wireMockServer.start();
                folder = File.createTempFile("tmp", ".tmp");
                githubEndpoint = new Api(API_URL, folder, () -> CLIENT_ID, () -> CLIENT_KEY).github();
            });

            after(() -> wireMockServer.stop());

            describe("When list Java repositories ordered by stars", () -> {

                beforeEach(() -> {
                    resetListeners();
                    githubEndpoint.searchRepositories("language:Java", "stars", 1)
                            .toBlocking()
                            .subscribe(onNext, onError, onComplete);
                });

                describe("When the request is valid", () -> {

                    before(this::stubForRepositories);

                    it("should return a SearchEvenlop", () -> {
                        verify(onNext).call(any(SearchEvenlope.class));
                        verify(onComplete).call();
                    });

                    it("should not call on error", () -> {
                        verifyZeroInteractions(onError);
                    });

                });

                checkInteractionsForFailure("/search/repositories");
            });

            describe("When list pull requests from 'facebook/react-native'", () -> {

                beforeEach(() -> {
                    resetListeners();
                    githubEndpoint.pullRequests("facebook", "react-native")
                            .toBlocking()
                            .subscribe(onNext, onError, onComplete);
                });

                describe("When the request is valid", () -> {

                    before(this::stubForPullRequests);

                    it("should return a List of PullRequests", () -> {
                        verify(onNext).call(any(List.class));
                        verify(onComplete).call();
                    });

                    it("should not call on error", () -> {
                        verifyZeroInteractions(onError);
                    });

                });

                checkInteractionsForFailure("/repos/facebook/react-native/pulls");

            });

        });
    }

    private void stubForPullRequests() throws FileNotFoundException {
        WireMock.reset();
        stubFor(get(urlPathEqualTo("/repos/facebook/react-native/pulls"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(resourceFile("resultPullRequestGood.json"))
                )
        );
    }

    private void stubForRepositories() throws FileNotFoundException {
        WireMock.reset();
        stubFor(get(urlPathEqualTo("/search/repositories"))
                .withQueryParam("q", equalTo("language:Java"))
                .withQueryParam("sort", equalTo("stars"))
                .withQueryParam("page", equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(resourceFile("resultRepositoryGood.json"))
                )
        );
    }

    private void checkInteractionsForFailure(String url) {
        describe("When the request is invalid", () -> {

            before(() -> {
                WireMock.reset();
                stubFor(get(urlPathEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(404)
                                .withBody(resourceFile("resultBad.json"))
                        )
                );
            });

            it("should not call on error", () -> {
                verify(onError).call(any(HttpException.class));
            });

            it("should no call onNext", () -> verifyZeroInteractions(onNext));

            it("should no call onComplete", () -> verifyZeroInteractions(onComplete));

        });
    }

    private void resetListeners() {
        onNext = mock(Action1.class);
        onError = mock(Action1.class);
        onComplete = mock(Action0.class);
    }
}