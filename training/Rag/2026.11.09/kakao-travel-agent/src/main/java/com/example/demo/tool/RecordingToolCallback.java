package com.example.demo.tool;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.metadata.ToolMetadata;

/**
 * 카카오맵 MCP 도구를 감싸서, 도구 결과에 들어 있는 장소 URL 을 KakaoPlaceRegistry 에 기록한다.
 * 도구 동작 자체는 그대로 원래 MCP 도구에 맡긴다.
 */
public class RecordingToolCallback implements ToolCallback {

	public static final String CONVERSATION_ID = "conversationId";

	private final ToolCallback delegate;
	private final KakaoPlaceRegistry registry;

	public RecordingToolCallback(ToolCallback delegate, KakaoPlaceRegistry registry) {
		this.delegate = delegate;
		this.registry = registry;
	}

	@Override
	public ToolDefinition getToolDefinition() {
		return delegate.getToolDefinition();
	}

	@Override
	public ToolMetadata getToolMetadata() {
		return delegate.getToolMetadata();
	}

	@Override
	public String call(String toolInput) {
		String result = delegate.call(toolInput);
		registry.record("default", result);
		return result;
	}

	@Override
	public String call(String toolInput, ToolContext toolContext) {
		String result = delegate.call(toolInput, toolContext);
		registry.record(conversationId(toolContext), result);
		return result;
	}

	static String conversationId(ToolContext toolContext) {
		if (toolContext == null) return "default";
		Object id = toolContext.getContext().get(CONVERSATION_ID);
		return id == null ? "default" : id.toString();
	}
}
