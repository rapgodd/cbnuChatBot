package com.giyeon.cbnuchatbot.service;

import com.giyeon.cbnuchatbot.config.VectorCollectionProperties;
import com.giyeon.cbnuchatbot.config.VectorStoreProperties;
import io.milvus.client.MilvusClient;
import io.milvus.grpc.DataType;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.collection.*;
import io.milvus.param.index.CreateIndexParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoadVectorCollectionServiceImpl implements LoadVectorCollectionService {

    @Autowired
    private MilvusClient milvusClient;

    @Autowired
    private VectorCollectionProperties vectorCollectionProperties;

    @Override
    public void recreateCollection() {
        String collectionName = vectorCollectionProperties.getCollectionName();
        String databaseName = vectorCollectionProperties.getDatabaseName();

        // Check if the collection exists and drop it if it does
        HasCollectionParam collectionParam = HasCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .withDatabaseName(databaseName)
                .build();

        if (milvusClient.hasCollection(collectionParam).getData()) {
            milvusClient.dropCollection(
                    DropCollectionParam.newBuilder()
                            .withCollectionName(collectionName)
                            .withDatabaseName(databaseName)
                            .build()
            );
        }

        // Define fields for the collection
        List<FieldType> fields = new ArrayList<>();
        fields.add(FieldType.newBuilder()
                .withName("doc_id")
                .withDataType(DataType.VarChar)
                .withPrimaryKey(true)
                .withMaxLength(100)
                .withAutoID(false)
                .build()
        );
        fields.add(FieldType.newBuilder()
                .withName("embedding")
                .withDataType(DataType.FloatVector)
                .withDimension(3072)
                .build()
        );
        fields.add(FieldType.newBuilder()
                .withName("content")
                .withDataType(DataType.VarChar)
                .withMaxLength(65000)
                .build()
        );
        fields.add(FieldType.newBuilder()
                .withName("metadata")
                .withDataType(DataType.JSON)
                .withMaxLength(2048)
                .build()
        );

        // Create the collection
        CreateCollectionParam createCollectionParam = CreateCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .withFieldTypes(fields)
                .withShardsNum(2)
                .build();

        milvusClient.createCollection(createCollectionParam);

        // Create an index for the embedding field
        CreateIndexParam createIndexParam = CreateIndexParam.newBuilder()
                .withCollectionName(collectionName)
                .withFieldName("embedding")
                .withIndexType(IndexType.IVF_FLAT)
                .withMetricType(MetricType.COSINE)
                .withExtraParam("{\"nlist\":128}")
                .build();

        milvusClient.createIndex(createIndexParam);

        // Load the collection into memory
        LoadCollectionParam loadCollectionParam = LoadCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .build();

        milvusClient.loadCollection(loadCollectionParam);
    }
}
