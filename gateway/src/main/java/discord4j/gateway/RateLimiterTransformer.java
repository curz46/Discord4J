/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J. If not, see <http://www.gnu.org/licenses/>.
 */

package discord4j.gateway;

import discord4j.common.RateLimiter;
import io.netty.buffer.ByteBuf;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Transforms a payload sequence to throttle it using a {@link RateLimiter} instance.
 */
public class RateLimiterTransformer implements PayloadTransformer {

    private final RateLimiter limiter;

    public RateLimiterTransformer(RateLimiter limiter) {
        this.limiter = limiter;
    }

    @Override
    public Publisher<ByteBuf> apply(Flux<ByteBuf> publisher) {
        return publisher.concatMap(payload -> Mono
                .<ByteBuf>create(sink -> {
                    if (limiter.tryConsume(1)) {
                        sink.success(payload);
                    } else {
                        sink.error(new RuntimeException());
                    }
                })
                .retryWhen(errors -> errors.concatMap(t ->
                        Mono.delay(Duration.ofMillis(limiter.delayMillisToConsume(1)), Schedulers.single()))), 1);
    }
}
