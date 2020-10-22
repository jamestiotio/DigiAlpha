# RAGUL BALAJI 2020
# SUTD 50.002 Computation Structures 2D WEEK!
# Print BC file stuffs to STDOUT

# BC File Header
print("BC1.1")

def RippleCarry32BC():
  # Generate XB
  for i in range(32):
    print("B%d := ODD(Bin%d,OP0);" % (i, i)) # invert B if op0

  # Handle OP0
  print("\nC0 := (OP0);")

  # Ripple Time!
  for i in range(32):
    print("C%d := OR(AND(A%d,B%d),AND(A%d,C%d),AND(B%d,C%d));" % (i+1, i, i, i, i, i, i)) # Ci+1 = (Ai AND Bi) OR (Ai AND Ci) OR (Bi AND Ci)
  
  print("") # Neat
  for i in range(32):
    print("S%d := ODD(A%d,B%d,C%d);" % (i, i, i, i)) # SUMi  = Ai XOR Bi XOR Ci

RippleCarry32BC()