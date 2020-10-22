def RippleCarry32BC():
  print("\n") # Neat
  # Generate XB
  for i in range(32):
    print("BINV%d := NOT(Bin%d);" % (i, i)) # invert B

  print("\n")

  print("OP0INV := NOT(OP0);")

  print("\n")

  # Implement hex inversion in BC
  for i in range(1, 5):
    print("SEL%d := NOT(OP0INV);" % i)

  for i in range(1, 5):
    print("\n")

    # Implement mux selectors using AND, OR and INV
    for j in range(8):
        k = 8 * (i - 1) + j
        print("M%d := AND(Bin%d, BINV%d);" % (k, k, k))
        print("N%d := AND(SEL%d, NOT(BINV%d));" % (k, i, k))
        print("B%d := OR(M%d, N%d);" % (k, k, k))

  print("\n")

  # Handle OP0
  print("\nC0 := (OP0);")

  # Ripple Time!
  for i in range(32):
    print("C%d := OR(AND(A%d,B%d),AND(A%d,C%d),AND(B%d,C%d));" % (i+1, i, i, i, i, i, i)) # Ci+1 = (Ai AND Bi) OR (Ai AND Ci) OR (Bi AND Ci)
  
  print("") # Neat
  for i in range(32):
    print("RSUM%d := ODD(A%d,B%d,C%d);" % (i, i, i, i)) # SUMi  = Ai XOR Bi XOR Ci

def HanCarlson32BC():
  print("\n") # Neat
  # Generate Propagate Cells

  for i in range(32):
    print("X%d := NOT(OR(A%d, B%d));" % (i, i, i)) # Xi = Ai NOR Bi

  print("\n")

  for i in range(32):
    print("L0P%d := NOT(OR(AND(A%d, B%d), X%d));" % (i, i, i, i)) # Pi = NOT((Ai AND Bi) OR Xi)

  print("\n")

  print("") # Neat
  print("g0pre := AND(A0,B0);") # kek0 = Ai AND Bi
  print("L0G0 := OR(AND(L0P0, OP0),g0pre);") # gg = (pt AND gl) OR gt
  for i in range(1,32):
    print("L0G%d := AND(A%d,B%d);" % (i, i, i)) # Gi = Ai AND Bi

  # Layer 1
  for i in ["G", "P"]:
    for j in range(0, 31, 2):
      print("L1%s%d := L0%s%d;" % (i, j, i, j))
  print("L1G1 := OR(AND(L0P1, L0G0), L0G1);")
  for i in range(3, 32, 2):
    print("L1P%d := AND(L0P%d, L0P%d);" % (i, i, i-1))
    print("L1G%d := OR(AND(L0P%d, L0G%d), L0G%d);" % (i, i, i-1, i))
  
  print("\n")

  # Layer 2
  print("L2G2 := L1G2;")
  print("L2P2 := L1P2;")
  print("L2P0 := L1P0;")
  print("L2G1 := L1G1;")
  print("L2G0 := L1G0;")
  for i in ["G", "P"]:
    for k in range(4, 7):
      for j in range(k, k+25, 4):
        print("L2%s%d := L1%s%d;" % (i, j, i, j))
  print("L2G3 := OR(AND(L1P3, L1G1), L1G3);")
  for i in range(7, 32, 4):
    print("L2P%d := AND(L1P%d, L1P%d);" % (i, i, i-2))
    print("L2G%d := OR(AND(L1P%d, L1G%d), L1G%d);" % (i, i, i-2, i))
  
  print("\n")

  # Layer 3
  print("L3P2 := L2P2;")
  print("L3P0 := L2P0;")
  print("L3G3 := L2G3;")
  print("L3G2 := L2G2;")
  print("L3G0 := L2G0;")
  for i in ["G", "P"]:
    for k in range(4, 7):
      for j in range(k, k+25, 4):
        print("L3%s%d := L2%s%d;" % (i, j, i, j))
  print("L3G7 := OR(AND(L2P7, L2G3), L2G7);")
  for i in range(11, 32, 4):
    print("L3P%d := AND(L2P%d, L2P%d);" % (i, i, i-4))
    print("L3G%d := OR(AND(L2P%d, L2G%d), L2G%d);" % (i, i, i-4, i))

  print("\n")
  
  # Layer 4
  print("L4P2 := L3P2;")
  print("L4P0 := L3P0;")
  print("L4G7 := L3G7;")
  for i in range(4, 7):
    print("L4P%d := L3P%d;" % (i, i))
  for i in range(7):
    print("L4G%d := L3G%d;" % (i, i))
  for i in ["G", "P"]:
    for k in range(8, 11):
      for j in range(k, k+21, 4):
        print("L4%s%d := L3%s%d;" % (i, j, i, j))
  print("L4G11 := OR(AND(L3P11, L3G3), L3G11);")
  print("L4G15 := OR(AND(L3P15, L3G7), L3G15);")
  for i in range(19, 32, 4):
    print("L4P%d := AND(L3P%d, L3P%d);" % (i, i, i-8))
    print("L4G%d := OR(AND(L3P%d, L3G%d), L3G%d);" % (i, i, i-8, i))
  
  print("\n")

  # Layer 5
  for i in range(11):
    print("L5G%d := L4G%d;" % (i, i))
  for k in ["G", "P"]:
    for i in range(12, 15):
      print("L5%s%d := L4%s%d;" % (k, i, k, i))
    for i in range(16, 19):
      print("L5%s%d := L4%s%d;" % (k, i, k, i))
  print("L5G15 := L4G15;")
  print("L5G11 := L4G11;")
  for i in range(8, 11):
    print("L5P%d := L4P%d;" % (i, i))
  for i in range(4, 7):
    print("L5P%d := L4P%d;" % (i, i))
  print("L5P2 := L4P2;")
  print("L5P0 := L4P0;")
  for k in ["G", "P"]:
    for i in range(20, 23):
      print("L5%s%d := L4%s%d;" % (k, i, k, i))
    for i in range(24, 27):
      print("L5%s%d := L4%s%d;" % (k, i, k, i))
    for i in range(28, 31):
      print("L5%s%d := L4%s%d;" % (k, i, k, i))
  for i in range(19, 32, 4):
    print("L5G%i := OR(AND(L4P%i, L4G%i), L4G%i);" % (i, i, i-16, i))
  
  print("\n")

  # Layer 6
  print("L6G27 := L5G27;")
  print("L6G31 := L5G31;")
  print("L6G19 := L5G19;")
  print("L6G23 := L5G23;")
  for i in range(5):
    print("L6G%d := L5G%d;" % (i, i))
  for i in range(2, 31, 2):
    print("L6P%d := L5P%d;" % (i, i))
  for i in range(6, 9):
    print("L6G%d := L5G%d;" % (i, i))
  for i in range(10, 13):
    print("L6G%d := L5G%d;" % (i, i))
  for i in range(14, 17):
    print("L6G%d := L5G%d;" % (i, i))
  for i in range(18, 31, 2):
    print("L6G%d := L5G%d;" % (i, i))
  for i in range(5, 30, 4):
    print("L6G%i := OR(AND(L5P%i, L5G%i), L5G%i);" % (i, i, i-2, i))

  print("\n")

  # Layer 7

  print("L7G0 := L6G0;")
  print("L7G1 := L6G1;")
  print("L7G29 := L6G29;")
  print("L7G17 := L6G17;")
  print("L7G21 := L6G21;")
  print("L7G25 := L6G25;")
  for i in range(3, 32, 4):
    print("L7G%d := L6G%d;" % (i, i))
  for i in range(5, 14, 4):
    print("L7G%d := L6G%d;" % (i, i))
  for i in range(2, 31, 2):
    print("L7G%i := OR(AND(L6P%i, L6G%i), L6G%i);" % (i, i, i-1, i))

  print("\n")

  # Compute Sum
  print("") # Neat
  print("PSUM0 := NOT(OR(OP0, L0P0));") # SPECIAL CASE
  for i in range(1,32):
    print("PSUM%d := NOT(OR(L0P%d, L7G%d));" % (i, i, i-1)) # NOR for psum
  print("") # Neat
  print("HSUM0 := NOT(OR(AND(L0P0, OP0), PSUM0));") # SPECIAL CASE
  for i in range(1,32):
    print("HSUM%d := NOT(OR(AND(L0P%d, L7G%d), PSUM%d));" % (i, i, i-1, i)) # AOI for sum

if __name__ == '__main__':
  print("BC1.1") # BC File Header
  RippleCarry32BC()
  HanCarlson32BC()

  # TEST IF MSB IS EQUAL
  print("\n\n") # Neat
  print("MUSTBEFALSE := ODD(RSUM31, HSUM31);")
  print("ASSIGN MUSTBEFALSE;")