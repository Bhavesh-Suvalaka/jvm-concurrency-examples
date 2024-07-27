package com.example.concurrency.services;

import com.example.concurrency.Utils;
import com.example.concurrency.models.Score;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

@SuppressWarnings("ALL")
public class CricketScoreService {
  public CompletableFuture<Score> getScore() {
    var scoreFromGoogleFuture = CompletableFuture.supplyAsync(this::getScoreFromGoogle);
    var scoreFromYahooFuture = CompletableFuture.supplyAsync(this::getScoreFromYahoo);
    var scoreFromHotstarFuture = CompletableFuture.supplyAsync(this::getScoreFromHotstar);

    return CompletableFuture.anyOf(scoreFromGoogleFuture, scoreFromYahooFuture, scoreFromHotstarFuture)
      .exceptionally(RuntimeException::new)
      .thenApplyAsync(Score.class::cast);
  }

  public Score getScoreStructured() {
    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Score>()) {
      scope.fork(this::getScoreFromGoogle);
      scope.fork(this::getScoreFromYahoo);
      scope.fork(this::getScoreFromHotstar);

      return scope.join().result();
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private Score getScoreFromYahoo() {
    Utils.sleep(100);
    return new Score(100);
  }

  private Score getScoreFromGoogle() {
    Utils.sleep(200);
    return new Score(100);
  }

  private Score getScoreFromHotstar() {
    Utils.sleep(50);
    return new Score(100);
  }
}
