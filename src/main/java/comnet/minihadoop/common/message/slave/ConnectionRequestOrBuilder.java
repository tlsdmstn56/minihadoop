// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/proto/slave.proto

package comnet.minihadoop.common.message.slave;

public interface ConnectionRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:slave.ConnectionRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string hostname = 1;</code>
   * @return The hostname.
   */
  java.lang.String getHostname();
  /**
   * <code>string hostname = 1;</code>
   * @return The bytes for hostname.
   */
  com.google.protobuf.ByteString
      getHostnameBytes();

  /**
   * <code>int32 port = 2;</code>
   * @return The port.
   */
  int getPort();

  /**
   * <code>int32 healthCheckPort = 3;</code>
   * @return The healthCheckPort.
   */
  int getHealthCheckPort();
}
