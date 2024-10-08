# 嵌入模型 (Embedding Model)

>[!TIP]
> 
> **嵌入(`Embedding`)的工作原理是将文本、图像和视频转换为称为向量（Vectors）的浮点数数组。这些向量旨在捕捉文本、图像和视频的含义。嵌入数组的长度称为向量的维度（Dimensionality）。**

**嵌入模型（`EmbeddingModel`）是嵌入过程中采用的模型。当前 EmbeddingModel 的接口主要用于将文本转换为数值向量，接口的设计主要围绕这两个目标展开：**

- **可移植性：该接口确保在各种嵌入模型之间的轻松适配。它允许开发者在不同的嵌入技术或模型之间切换，所需的代码更改最小化。这一设计与 Spring 模块化和互换性的理念一致。**
- **简单性：嵌入模型简化了文本转换为嵌入的过程。通过提供如`embed(String text)`和`embed(Document document)`这样简单的方法，它去除了处理原始文本数据和嵌入算法的复杂性。这个设计选择使开发者，尤其是那些初次接触 AI 的开发者，更容易在他们的应用程序中使用嵌入，而无需深入了解其底层机制。**

# EmbeddingModel API介绍

```java
package org.springframework.ai.embedding;

import org.springframework.ai.document.Document;
import org.springframework.ai.model.Model;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * EmbeddingModel is a generic interface for embedding models.
 *
 * @author Mark Pollack
 * @author Christian Tzolov
 * @author Josh Long
 * @author Soby Chacko
 * @since 1.0.0
 *
 */
public interface EmbeddingModel extends Model<EmbeddingRequest, EmbeddingResponse> {

	@Override
	EmbeddingResponse call(EmbeddingRequest request);

	/**
	 * Embeds the given text into a vector.
	 * @param text the text to embed.
	 * @return the embedded vector.
	 */
	default float[] embed(String text) {
		Assert.notNull(text, "Text must not be null");
		List<float[]> response = this.embed(List.of(text));
		return response.iterator().next();
	}

	/**
	 * Embeds the given document's content into a vector.
	 * @param document the document to embed.
	 * @return the embedded vector.
	 */
	float[] embed(Document document);

	/**
	 * Embeds a batch of texts into vectors.
	 * @param texts list of texts to embed.
	 * @return list of list of embedded vectors.
	 */
	default List<float[]> embed(List<String> texts) {
		Assert.notNull(texts, "Texts must not be null");
		return this.call(new EmbeddingRequest(texts, EmbeddingOptionsBuilder.builder().build()))
			.getResults()
			.stream()
			.map(Embedding::getOutput)
			.toList();
	}

	/**
	 * Embeds a batch of {@link Document}s into vectors based on a
	 * {@link BatchingStrategy}.
	 * @param documents list of {@link Document}s.
	 * @param options {@link EmbeddingOptions}.
	 * @param batchingStrategy {@link BatchingStrategy}.
	 * @return a list of float[] that represents the vectors for the incoming
	 * {@link Document}s.
	 */
	default List<float[]> embed(List<Document> documents, EmbeddingOptions options, BatchingStrategy batchingStrategy) {
		Assert.notNull(documents, "Documents must not be null");
		List<float[]> embeddings = new ArrayList<>();

		List<List<Document>> batch = batchingStrategy.batch(documents);

		for (List<Document> subBatch : batch) {
			List<String> texts = subBatch.stream().map(Document::getContent).toList();
			EmbeddingRequest request = new EmbeddingRequest(texts, options);
			EmbeddingResponse response = this.call(request);
			for (int i = 0; i < subBatch.size(); i++) {
				Document document = subBatch.get(i);
				float[] output = response.getResults().get(i).getOutput();
				embeddings.add(output);
				document.setEmbedding(output);
			}
		}
		return embeddings;
	}

	/**
	 * Embeds a batch of texts into vectors and returns the {@link EmbeddingResponse}.
	 * @param texts list of texts to embed.
	 * @return the embedding response.
	 */
	default EmbeddingResponse embedForResponse(List<String> texts) {
		Assert.notNull(texts, "Texts must not be null");
		return this.call(new EmbeddingRequest(texts, EmbeddingOptionsBuilder.builder().build()));
	}

	/**
	 * @return the number of dimensions of the embedded vectors. It is generative
	 * specific.
	 */
	default int dimensions() {
		return embed("Test String").length;
	}

}

```

Embedding Model API 提供多种选项，将文本转换为 Embeddings，支持单个字符串、结构化的 Document 对象或文本批处理。有多种快捷方式可以获得文本 Embeddings。例如`embed(String text)`方法，它接受单个字符串并返回相应的 Embedding 向量。所有方法都围绕着`call`方法实现，这是调用 Embedding Model 的主要方法。通常，Embedding 返回一个 float 数组，以数值向量格式表示Embeddings。

- `embedForResponse`方法提供了更全面的输出，可能包括有关 Embeddings 的其他信息。
- `dimensions`方法是开发人员快速确定 Embedding 向量大小的便利工具，这对于理解 Embedding space 和后续处理步骤非常重要。

`EmbeddingRequest`是一种`ModelRequest`，它接受文本对象列表和可选的 Embedding 请求选项。以下代码片段简要地显示了 EmbeddingRequest 类，省略了构造函数和其他工具方法：

```java
public class EmbeddingRequest implements ModelRequest<List<String>> {
private final List<String> inputs;
private final EmbeddingOptions options;
// other methods omitted
}
```

`EmbeddingResponse`类的结构如下：

```java
public class EmbeddingResponse implements ModelResponse<Embedding> {
    private List<Embedding> embeddings;
    private EmbeddingResponseMetadata metadata = new EmbeddingResponseMetadata();
    // other methods omitted
}
```

`EmbeddingResponse`类保存了 AI 模型的输出，其中每个 Embedding 实例包含来自单个文本输入的结果向量数据。同时，它还携带了有关 AI 模型响应的`EmbeddingResponseMetadata`元数据。`Embedding`表示一个 Embedding 向量。

```java
public class Embedding implements ModelResult<List<Double>> {
    private List<Double> embedding;
    private Integer index;
    private EmbeddingResultMetadata metadata;
// other methods omitted
}
```

# 示例用法

该示例将创建一个`EmbeddingModel`实例，您可以将其注入到您的类中。以下是一个简单的`@Controller`类的示例，它使用了该 EmbeddingModel 实例。

```java
@RestController
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    @Autowired
    public EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/ai/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }
}
```

```http
###
GET http://localhost:8083/demo03/embed
```

![image-20241008200906933](./../assets/image-20241008200906933.png)

# 自定义

```java
@Configuration
public class AppConfig {
    DashScopeApi dashScopeApi = new DashScopeApi(System.getenv("DASHSCOPE_API_KEY"));

    @Bean
    public EmbeddingModel embeddingModel() {
        return new DashScopeEmbeddingModel(
            dashScopeApi,
            MetadataMode.EMBED,
            DashScopeEmbeddingOptions.builder()
                .withModel("text-embedding-v2")
                .build());
    }
}



    @GetMapping("/similar")
    public Map<String, EmbeddingResponse> similar() {
        EmbeddingResponse embeddingResponse = this.embeddingModel
            .embedForResponse(List.of("Hello World", "World is big and salvation is near"));
        logger.info("dimensions {}",this.embeddingModel.dimensions());
        return Map.of("embed", embeddingResponse);
    }


```

```java
###
GET http://localhost:8083/demo03/similar
```

![image-20241008200922261](./../assets/image-20241008200922261.png)

![image-20241008201447396](./../assets/image-20241008201447396.png)
