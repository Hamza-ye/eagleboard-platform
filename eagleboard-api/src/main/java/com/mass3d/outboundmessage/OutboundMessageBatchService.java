package com.mass3d.outboundmessage;

import java.util.List;

public interface OutboundMessageBatchService {

  List<OutboundMessageResponseSummary> sendBatches(List<OutboundMessageBatch> batches);
}
