{
    # Class names of motor controllers used.
    # Options:
    # 'WPI_TalonSRX'
    # 'WPI_VictorSPX'
    "rightControllerTypes": ["WPI_TalonSRX", "WPI_VictorSPX", "WPI_VictorSPX"],
    "leftControllerTypes": ["WPI_TalonSRX", "WPI_VictorSPX", "WPI_VictorSPX"],

    # Ports for the left-side motors
    "leftMotorPorts": [1, 7, 12],
    # Ports for the right-side motors
    "rightMotorPorts": [2, 8, 11],

    # Inversions for the left-side motors
    "leftMotorsInverted": [False, False, False],
    # Inversions for the right side motors
    "rightMotorsInverted": [False, False, False],

    # Wheel diameter (in units of your choice - will dictate units of analysis)
    "wheelDiameter": 0.157,

    # If your robot has only one encoder, remove all of the right encoder fields
    # Encoder pulses-per-revolution (*NOT* cycles per revolution!)
    # This value should be the pulses per revolution *of the wheels*, and so
    # should take into account gearing between the encoder and the wheels
    "encoderPPR": 4048,

    # Whether the left encoder is inverted
    "leftEncoderInverted": True,
    # Whether the right encoder is inverted:
    "rightEncoderInverted": False,
    # Your gyro type (one of "NavX", "Pigeon", "ADXRS450", "AnalogGyro", or "None")
    "gyroType": "NavX",
    # Whatever you put into the constructor of your gyro
    # Could be:
    # "SPI.Port.kMXP" (MXP SPI port for NavX or ADXRS450),
    # "I2C.Port.kOnboard" (Onboard I2C port for NavX)
    # "0" (Pigeon CAN ID or AnalogGyro channel),
    # "new TalonSRX(3)" (Pigeon on a Talon SRX),
    # "leftSlave" (Pigeon on the left slave Talon SRX),
    # "" (NavX using default SPI, ADXRS450 using onboard CS0, or no gyro)
    "gyroPort": "SPI.Port.kMXP",
}



