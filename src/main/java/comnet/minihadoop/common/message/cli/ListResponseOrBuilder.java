// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/proto/cli.proto

package comnet.minihadoop.common.message.cli;

public interface ListResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:cli.ListResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .cli.ListResponse.File files = 1;</code>
   */
  java.util.List<comnet.minihadoop.common.message.cli.ListResponse.File> 
      getFilesList();
  /**
   * <code>repeated .cli.ListResponse.File files = 1;</code>
   */
  comnet.minihadoop.common.message.cli.ListResponse.File getFiles(int index);
  /**
   * <code>repeated .cli.ListResponse.File files = 1;</code>
   */
  int getFilesCount();
  /**
   * <code>repeated .cli.ListResponse.File files = 1;</code>
   */
  java.util.List<? extends comnet.minihadoop.common.message.cli.ListResponse.FileOrBuilder> 
      getFilesOrBuilderList();
  /**
   * <code>repeated .cli.ListResponse.File files = 1;</code>
   */
  comnet.minihadoop.common.message.cli.ListResponse.FileOrBuilder getFilesOrBuilder(
      int index);
}
