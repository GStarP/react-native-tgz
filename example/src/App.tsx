import * as React from 'react';
import * as FS from 'expo-file-system';

import { StyleSheet, View, Text } from 'react-native';
import { decompress } from 'react-native-tgz';

export default function App() {
  const [result, setResult] = React.useState<string>('');

  React.useEffect(() => {
    FS.readDirectoryAsync(FS.documentDirectory!)
      .then((res) => setResult(res.toString()))
      .then(() =>
        FS.downloadAsync(
          'https://registry.npmjs.org/@board-game-toolbox/plugin-template/-/plugin-template-0.1.3.tgz',
          FS.documentDirectory + 'plugin-template.tgz'
        )
      )
      .then(() => FS.readDirectoryAsync(FS.documentDirectory!))
      .then((res) => setResult(res.toString()))
      .then(() =>
        decompress(
          FS.documentDirectory + 'plugin-template.tgz',
          FS.documentDirectory ?? ''
        )
      )
      .then(() => FS.readDirectoryAsync(FS.documentDirectory!))
      .then((res) => setResult(res.toString()))
      .catch((e) => {
        console.error(e);
      });
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
