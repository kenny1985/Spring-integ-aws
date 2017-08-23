package org.springframework.integration.aws;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizer;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizingMessageSource;
import org.springframework.integration.aws.support.filters.S3RegexPatternFileListFilter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.messaging.PollableChannel;

import java.io.File;

	@SpringBootApplication
	@EnableConfigurationProperties(ServiceProperties.class)
	public class SpringIntegApplication {

		/*@Bean
		public  FileReadingMessageSource fileReader() {
			FileReadingMessageSource reader = new FileReadingMessageSource();
			reader.setDirectory(new File("target/input"));
			return reader;
		}

		@Bean
		public  DirectChannel inputChannel() {
			return new DirectChannel();
		}

		@Bean
		public  DirectChannel outputChannel() {
			return new DirectChannel();
		}

		@Bean
		public  FileWritingMessageHandler fileWriter() {
			FileWritingMessageHandler writer = new FileWritingMessageHandler(
					new File("target/output"));
			writer.setExpectReply(false);
			return writer;
		}

		@Bean
		public  IntegrationFlow integrationFlow(SampleEndpoint endpoint) {
			return IntegrationFlows.from(fileReader(), sourcePollingChannelAdapterSpec -> { sourcePollingChannelAdapterSpec.poller(Pollers.fixedRate(500));})
					.channel(inputChannel()).handle(endpoint).channel(outputChannel())
					.handle(fileWriter()).get();
		}*/

		public static  void main(String[] args) throws Exception {
			SpringApplication.run(SpringIntegApplication.class, args);
		}

		/*private static class FixedRatePoller
				implements Consumer<SourcePollingChannelAdapterSpec> {

			@Override
			public  void accept(SourcePollingChannelAdapterSpec spec) {
				spec.poller(Pollers.fixedRate(500));
			}

		}*/

		/*private String accessKey = "AKIAJZADOCX5KNMULOLA";

		private String secretKey ="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";

		private String s3BucketName = "cf-24001995-d49b-4e3b-b28e-6fdafea06273";*/

		private String accessKey = "F585ZB6XIBKB1NUP577C";

		private String secretKey ="JTP/lHBW5wW+upJ1kZM5YRUDaKWavNV+5IPNKPQz";

		private String s3BucketName = "moviefun";

		public AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion("ap-southeast-1")
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey))).build();



		@Bean
			public S3InboundFileSynchronizer s3InboundFileSynchronizer() {


			S3InboundFileSynchronizer synchronizer = new S3InboundFileSynchronizer(amazonS3);
			synchronizer.setDeleteRemoteFiles(true);
			synchronizer.setPreserveTimestamp(true);

			synchronizer.setRemoteDirectory(s3BucketName.concat("/").concat(""));
			synchronizer.setFilter(new S3RegexPatternFileListFilter(".*\\.dat\\.{0,1}\\d{0,2}"));
			return synchronizer;
		}

		@Bean
		@InboundChannelAdapter(value = "s3FilesChannel", poller = @Poller(fixedDelay = "10"))
		public S3InboundFileSynchronizingMessageSource s3InboundFileSynchronizingMessageSource() {
			S3InboundFileSynchronizingMessageSource messageSource =
					new S3InboundFileSynchronizingMessageSource(s3InboundFileSynchronizer());
			messageSource.setAutoCreateLocalDirectory(true);

			messageSource.setLocalDirectory(new File("target/output"));
			messageSource.setLocalFilter(new AcceptOnceFileListFilter<>());
			return messageSource;
		}

		@Bean
		public PollableChannel s3FilesChannel() {
			return new QueueChannel();
		}

		@Bean
		IntegrationFlow fileReadingFlow(SampleEndpoint endpoint) {
			return IntegrationFlows
					.from(s3InboundFileSynchronizingMessageSource(),
							e -> e.poller(p -> p.fixedDelay(30, java.util.concurrent.TimeUnit.SECONDS)))
					.handle(endpoint)
					.get();
		}


	}