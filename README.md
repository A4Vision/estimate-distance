# estimate-distance using rssi ("rssi calibration")
Exeprmintal application that runs in background, collects data - RSSI, distance, phone model, phone position

## Distance calculation
We calculate a distance estimation using naive round trip time:

Assume a signal leaves device `x` at time `s0` (according to `x` subjectime timeline),
and arrives to device `y` at time `t0` (according to `y` subjective timeline).

Assume another signal leaves device `y` at time `t1` (according to `y` subjectime timeline),
and arrives to device `y` at time `s1` (according to `x` subjective timeline).

Then writing the following equations, we extract the distance:
```
s1 = t0 + distance / sound_speed + TIMELINES_OFFSET
t1 = s0 + distance / sound_speed - TIMELINES_OFFSET

distance = (s1 - s0 + t1 - t0) / 2 * sound_speed
```
### Open problems
For example distance claculation, see the notebook review_distance_calculation.ipynb

This distance estimation method has many drawbacks:
   - Devices with a weak microphone are hardly heard.
   - Naive implementation of signal processing is battery consuming.
   - It is not stable on every device, due to unknown reasons - we get constant offsets (1-3 meters error) from the real distance. Currently the main suspects are:
    * Some vocal phenomena happens in certain devices when they are playing
    * OS is not playing the sound track cleanly when using MediaPlayer naively the way we do
    * OS drops some bytes when we use AudioRecord the way we do
    * We drop some bytes accidentaly during processing

## Next steps
1. Copy paste open trace code, to run in a foreground service 
2. Copy paste open trace bluetooth code to share an id and rssi
3. Record gyro measurements
4. Support persitency of the id (no need to support persistency of all the data - cope with the happy flow only)
5. Send data to server
Iteratively:
6. Distribute
7. Anlayze data
