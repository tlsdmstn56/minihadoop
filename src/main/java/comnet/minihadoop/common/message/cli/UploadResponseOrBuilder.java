// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/proto/cli.proto

package comnet.minihadoop.common.message.cli;

public interface UploadResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:cli.UploadResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.cli.UploadResponse.Type status = 1;</code>
   * @return The enum numeric value on the wire for status.
   */
  int getStatusValue();
  /**
   * <code>.cli.UploadResponse.Type status = 1;</code>
   * @return The status.
   */
  comnet.minihadoop.common.message.cli.UploadResponse.Type getStatus();

  /**
   * <code>string hostname = 2;</code>
   * @return The hostname.
   */
  java.lang.String getHostname();
  /**
   * <code>string hostname = 2;</code>
   * @return The bytes for hostname.
   */
  com.google.protobuf.ByteString
      getHostnameBytes();

  /**
   * <code>int32 port = 3;</code>
   * @return The port.
   */
  int getPort();
}