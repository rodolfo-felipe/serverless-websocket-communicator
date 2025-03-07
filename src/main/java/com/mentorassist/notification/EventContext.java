package com.mentorassist.notification;

import com.amazonaws.services.lambda.runtime.RequestHandler;

interface EventContext<I,O> {
	Class<I> getInputClass();
	Class<O> getOutputClass();
	RequestHandler<I,O> getHandler();
}