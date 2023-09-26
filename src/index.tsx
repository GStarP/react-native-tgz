import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-tgz' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const Tgz = NativeModules.Tgz
  ? NativeModules.Tgz
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

/**
 * @param archivePath .tgz file path
 * @param targetPath target dir path
 * @example decompress("test.tgz", "temp") will create a directory "temp/test"
 * @attention auto cover existing files!!!
 * @returns targetPath
 */
export function decompress(
  archivePath: string,
  targetPath: string
): Promise<string> {
  return Tgz.decompress(archivePath, targetPath);
}
