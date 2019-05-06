/*
 * Copyright (c) 2011-2017 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Learn how to deal with errors.
 *
 * @author Sebastien Deleuze
 * @see Exceptions#propagate(Throwable)
 */
public class Part07Errors {

	Mono<User> betterCallSaulForBogusMono(Mono<User> mono) {
		return mono.onErrorReturn(User.SAUL);
	}

	Flux<User> betterCallSaulAndJesseForBogusFlux(Flux<User> flux) {
		return flux.onErrorResume(exception -> {
			return Flux.just(User.SAUL, User.JESSE);
		});
	}

	Flux<User> capitalizeMany(Flux<User> flux) {
		return flux.flatMap(user -> {
			try {
				return Mono.just(capitalizeUser(user));
			} catch (GetOutOfHereException e1) {
				return Flux.error(new GetOutOfHereException());
			}
		});
	}

	User capitalizeUser(User user) throws GetOutOfHereException {
		if (user.equals(User.SAUL)) {
			throw new GetOutOfHereException();
		}
		return new User(user.getUsername(), user.getFirstname(), user.getLastname());
	}

	protected final class GetOutOfHereException extends Exception {
	}

}
