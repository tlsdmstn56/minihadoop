// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/proto/slave.proto

package comnet.minihadoop.common.message.slave;

public final class Slave {
  private Slave() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_slave_Job_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_slave_Job_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_slave_ConnectionRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_slave_ConnectionRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_slave_ConnectionResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_slave_ConnectionResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_slave_UploadRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_slave_UploadRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_slave_UploadResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_slave_UploadResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_slave_ReplicateReadyRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_slave_ReplicateReadyRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_slave_ReplicateReadyResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_slave_ReplicateReadyResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_slave_ReplicateRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_slave_ReplicateRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_slave_ReplicateResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_slave_ReplicateResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\032src/main/proto/slave.proto\022\005slave\"\273\003\n\003" +
      "Job\022\035\n\004type\030\001 \001(\0162\017.slave.Job.Type\022-\n\rup" +
      "loadRequest\030\020 \001(\0132\024.slave.UploadRequestH" +
      "\000\0223\n\020replicateRequest\030\021 \001(\0132\027.slave.Repl" +
      "icateRequestH\000\022?\n\027replicationReadyReques" +
      "t\030\022 \001(\0132\034.slave.ReplicateReadyRequestH\000\022" +
      "5\n\021replicateResponse\030\023 \001(\0132\030.slave.Repli" +
      "cateResponseH\000\022A\n\030replicationReadyRespon" +
      "se\030\024 \001(\0132\035.slave.ReplicateReadyResponseH" +
      "\000\"n\n\004Type\022\016\n\nUPLOAD_REQ\020\000\022\027\n\023REPLICATE_R" +
      "EADY_REQ\020\001\022\021\n\rREPLICATE_REQ\020\002\022\027\n\023REPLICA" +
      "TE_READY_RES\020\003\022\021\n\rREPLICATE_RES\020\004B\006\n\004dat" +
      "a\"L\n\021ConnectionRequest\022\020\n\010hostname\030\001 \001(\t" +
      "\022\014\n\004port\030\002 \001(\005\022\027\n\017healthCheckPort\030\003 \001(\005\"" +
      "U\n\022ConnectionResponse\022\013\n\003ack\030\001 \001(\005\022\031\n\021he" +
      "althCheckPeriod\030\002 \001(\005\022\027\n\017healthCheckPort" +
      "\030\003 \001(\005\"\306\001\n\rUploadRequest\022\026\n\016clientHostna" +
      "me\030\001 \001(\t\022\022\n\nclientPort\030\002 \001(\005\022\020\n\010filename" +
      "\030\003 \001(\t\022\016\n\006userID\030\004 \001(\t\022\017\n\007initial\030\005 \001(\010\022" +
      "\026\n\016slave1Hostname\030\006 \001(\t\022\022\n\nslave1Port\030\007 " +
      "\001(\005\022\026\n\016slave2Hostname\030\010 \001(\t\022\022\n\nslave2Por" +
      "t\030\t \001(\005\"[\n\016UploadResponse\022*\n\006status\030\001 \001(" +
      "\0162\032.slave.UploadResponse.Type\"\035\n\004Type\022\013\n" +
      "\007SUCCESS\020\000\022\010\n\004FAIL\020\001\"9\n\025ReplicateReadyRe" +
      "quest\022\020\n\010filename\030\003 \001(\t\022\016\n\006userID\030\004 \001(\t\"" +
      "8\n\026ReplicateReadyResponse\022\020\n\010hostname\030\001 " +
      "\001(\t\022\014\n\004port\030\002 \001(\005\"T\n\020ReplicateRequest\022\020\n" +
      "\010hostname\030\001 \001(\t\022\014\n\004port\030\002 \001(\005\022\020\n\010filenam" +
      "e\030\003 \001(\t\022\016\n\006userID\030\004 \001(\t\"a\n\021ReplicateResp" +
      "onse\022-\n\006status\030\001 \001(\0162\035.slave.ReplicateRe" +
      "sponse.Type\"\035\n\004Type\022\013\n\007SUCCESS\020\000\022\010\n\004FAIL" +
      "\020\001B*\n&comnet.minihadoop.common.message.s" +
      "laveP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_slave_Job_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_slave_Job_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_slave_Job_descriptor,
        new java.lang.String[] { "Type", "UploadRequest", "ReplicateRequest", "ReplicationReadyRequest", "ReplicateResponse", "ReplicationReadyResponse", "Data", });
    internal_static_slave_ConnectionRequest_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_slave_ConnectionRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_slave_ConnectionRequest_descriptor,
        new java.lang.String[] { "Hostname", "Port", "HealthCheckPort", });
    internal_static_slave_ConnectionResponse_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_slave_ConnectionResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_slave_ConnectionResponse_descriptor,
        new java.lang.String[] { "Ack", "HealthCheckPeriod", "HealthCheckPort", });
    internal_static_slave_UploadRequest_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_slave_UploadRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_slave_UploadRequest_descriptor,
        new java.lang.String[] { "ClientHostname", "ClientPort", "Filename", "UserID", "Initial", "Slave1Hostname", "Slave1Port", "Slave2Hostname", "Slave2Port", });
    internal_static_slave_UploadResponse_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_slave_UploadResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_slave_UploadResponse_descriptor,
        new java.lang.String[] { "Status", });
    internal_static_slave_ReplicateReadyRequest_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_slave_ReplicateReadyRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_slave_ReplicateReadyRequest_descriptor,
        new java.lang.String[] { "Filename", "UserID", });
    internal_static_slave_ReplicateReadyResponse_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_slave_ReplicateReadyResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_slave_ReplicateReadyResponse_descriptor,
        new java.lang.String[] { "Hostname", "Port", });
    internal_static_slave_ReplicateRequest_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_slave_ReplicateRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_slave_ReplicateRequest_descriptor,
        new java.lang.String[] { "Hostname", "Port", "Filename", "UserID", });
    internal_static_slave_ReplicateResponse_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_slave_ReplicateResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_slave_ReplicateResponse_descriptor,
        new java.lang.String[] { "Status", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
